package com.paytm.paylite.wallet.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.paylite.wallet.controller.dto.request.ConfirmOrCancelDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateCustomerWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerAuthorizeDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCaptureDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackV3DTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantCampaignCashbackDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantCampaignTopupDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantRefundDTO;
import com.paytm.paylite.wallet.controller.dto.request.PrepareCustomerCashbackExpiryDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.CreateCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.TopupCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.response.P2PResponseDTO;
import com.paytm.paylite.wallet.controller.dto.response.TransactionV2DTO;
import com.paytm.paylite.wallet.controller.exception.IdempotentException;
import com.paytm.paylite.wallet.domain.wallet.CustomerWallet;
import com.paytm.paylite.wallet.domain.wallet.MerchantWallet;
import com.paytm.paylite.wallet.domain.wallet.WalletFactory;
import com.paytm.paylite.wallet.domain.wallet.WalletService;
import com.paytm.paylite.wallet.domain.wallet.account.Account;
import com.paytm.paylite.wallet.domain.wallet.account.AccountRepository;
import com.paytm.paylite.wallet.domain.wallet.account.AccountType;
import com.paytm.paylite.wallet.domain.wallet.account.CashbackExpiryRepository;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransactionRepository;
import com.paytm.paylite.wallet.domain.wallet.account.TransactionRepository;
import com.paytm.paylite.wallet.domain.wallet.account.monetary.Currency;
import com.paytm.paylite.wallet.domain.wallet.account.monetary.Money;
import com.paytm.paylite.wallet.infrastructure.idempotency.IdempotencyService;
import com.paytm.paylite.wallet.infrastructure.message.message.EventPayload;
import com.paytm.paylite.wallet.infrastructure.message.topic.CashbackDeposit;
import com.paytm.paylite.wallet.infrastructure.message.topic.ExpireConfirmCompletion;
import com.paytm.paylite.wallet.infrastructure.message.topic.ExpiredCashback;
import com.paytm.paylite.wallet.infrastructure.message.topic.PaymentCapture;
import com.paytm.paylite.wallet.infrastructure.uidgenerator.UuidGenerator;
import com.paytm.paylite.wallet.infrastructure.utils.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = {"wallet.transaction.p2p.maximum=100000","wallet.transaction.p2p.minimum=2",
    "wallet.daily.max=50000","wallet.monthly.max=1000000"})
public class CustomerWalletApplicationServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired private CustomerWalletApplicationService customerWalletApplicationService;
  @Autowired private WalletService walletService;
  @Autowired private WalletFactory walletFactory;
  @Autowired private TransactionRepository transactionRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private DerivedTransactionRepository derivedTransactionRepository;
  @Autowired private CashbackExpiryRepository cashbackExpiryRepository;
  
  @Autowired private PaymentCapture paymentCapture;
  @Autowired private CashbackDeposit cashbackDeposit;
  @Autowired private MessageCollector messageCollector;
  @Autowired private ExpiredCashback expiredCashback;
  @Autowired private ExpireConfirmCompletion expireConfirmCompletion;
  @Autowired private IdempotencyService idempotencyService;
  private String customerId = "111";
  private String merchantId1 = "221";
  private String merchantId2 = "222";

  private CustomerWallet customerWallet;
  private MerchantWallet merchantWallet;
  private String nonKycCustomerId = "nonkyc";
  private String merchantId = "merchant";
  private Money depositAmount = new Money(Currency.JPY, 10);
  private Money amount = new Money(Currency.JPY, 1);
  private Map<String, String> metadata = new HashMap<>();
  private String campaignToken = "123";

  CustomerWallet kycCustomerWallet;
  CustomerWallet kycCustomerWallet1;

  @Before
  public void setUp() {
    walletService.createCustomerWallet(new CreateCustomerWalletDTO(customerId));
    walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantId1));
    walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantId2));
    Account source = walletFactory.rebuildExternalWallet().source();
    Account budget = walletFactory.rebuildSystemWallet().budget();
    source.transfer(
        UuidGenerator.getUID(),
        budget,
        new Money(Currency.JPY, 10000),
        new Money(Currency.JPY, 10000),
        null);
    customerWallet = (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(nonKycCustomerId));
    merchantWallet = (MerchantWallet) walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantId));

    kycCustomerWallet =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    kycCustomerWallet.createAccount(
        new Account(kycCustomerWallet, AccountType.EMONEY, Currency.JPY, true, null));
    kycCustomerWallet1 =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    kycCustomerWallet1.createAccount(
        new Account(kycCustomerWallet1, AccountType.EMONEY, Currency.JPY, true, null));
    metadata.put("key", "value");
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    walletService.topupCampaignAccount(new TopupCampaignAccountDTO(campaignToken, new Money(10000)));
  }

  @After
  public void tearDown() {
    messageCollector.forChannel(paymentCapture.output()).clear();
    messageCollector.forChannel(cashbackDeposit.output()).clear();
    messageCollector.forChannel(expiredCashback.output()).clear();
    messageCollector.forChannel(expireConfirmCompletion.output()).clear();
  }
  
  @Test
  public void captureAccept() throws InterruptedException, JsonProcessingException {
    BlockingQueue<Message<?>> queue = messageCollector.forChannel(paymentCapture.output());
    long pid1 = walletService.prepare(new CustomerPrepareDTO(customerId, merchantId1, new Money(Currency.JPY, 100), null));
    long pid2 = walletService.prepare(new CustomerPrepareDTO(customerId, merchantId2, new Money(Currency.JPY, 100), null));
    customerWalletApplicationService.captureAccept("1", new CustomerCaptureDTO(toPids(pid1), null));
    customerWalletApplicationService.captureAccept("2", new CustomerCaptureDTO(toPids(pid2), null));
    assertThat(queue.size()).isEqualTo(2);
    Message<?> message1 = queue.take();
    Message<?> message2 = queue.take();

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(message1);
    assertThat(message1.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(merchantId1);
    assertThat(message2.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(merchantId2);
    EventPayload eventPayload1 = (EventPayload) message1.getPayload();
    EventPayload eventPayload2 = (EventPayload) message2.getPayload();
    CustomerCaptureDTO customerCaptureDTO1 = (CustomerCaptureDTO) eventPayload1.getData();
    CustomerCaptureDTO customerCaptureDTO2 = (CustomerCaptureDTO) eventPayload2.getData();
    assertThat(customerCaptureDTO1.getPids().size()).isEqualTo(1);
    assertThat(customerCaptureDTO1.getPids().get(0)).isEqualTo(pid1);
    assertThat(customerCaptureDTO2.getPids().size()).isEqualTo(1);
    assertThat(customerCaptureDTO2.getPids().get(0)).isEqualTo(pid2);
  }

  @Test
  public void cashbackV4Accept_should_publish_the_message() throws InterruptedException, JsonProcessingException {
    BlockingQueue<Message<?>> queue = messageCollector.forChannel(cashbackDeposit.output());

    customerWalletApplicationService.cashbackDepositAccept(
        "1",
        CustomerCashbackV3DTO.builder()
            .accountType(AccountType.CASHBACK.name())
            .customer_wallet_owner_id(customerWallet.getUserId())
            .amount(new Money(Currency.JPY, 100))
            .campaign_token(campaignToken).metadata(new HashMap<>()).build());

    assertThat(queue.size()).isEqualTo(1);
    Message<?> message = queue.take();
    assertThat(message.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(campaignToken);
    EventPayload eventPayload = (EventPayload) message.getPayload();
    assertThat(eventPayload.getHeader().getIdemKey()).isEqualTo("1");
    assertThat(eventPayload.getData()).isInstanceOf(CustomerCashbackV3DTO.class);
    CustomerCashbackV3DTO data = (CustomerCashbackV3DTO) eventPayload.getData();
    assertThat(data.amount.getCents()).isEqualTo(100);
    assertThat(data.campaign_token).isEqualTo(campaignToken);
    assertThat(data.getAccountType()).isEqualTo("CASHBACK");
  }

  @Test
  public void cashbackV5Process_reponse_should_match_idempotencyservice() throws Exception {

  }

  @Test
  public void p2pKyc2NonKyc() {
    fundingKycCustomerWallet();
    P2PResponseDTO p2PResponseDTO = customerWalletApplicationService.p2p(UUID.randomUUID().toString(), new CustomerP2PDTO(kycCustomerWallet.getUserId(), customerWallet.getUserId(), new Money(Currency.JPY, 1000), new HashMap<>()));
    Assertions.assertThat(p2PResponseDTO.getSenderPrepaidAmount()).isEqualTo(800);
    Assertions.assertThat(p2PResponseDTO.getSenderEmoneyAmount()).isEqualTo(200);
    Assertions.assertThat(p2PResponseDTO.getReceiverPrepaidAmount()).isEqualTo(1000);
    Assertions.assertThat(p2PResponseDTO.getReceiverEmoneyAmount()).isEqualTo(0);
  }

  @Test
  public void p2pKyc2Kyc() {
    fundingKycCustomerWallet();
    P2PResponseDTO p2PResponseDTO = customerWalletApplicationService.p2p(UUID.randomUUID().toString(), new CustomerP2PDTO(kycCustomerWallet.getUserId(), kycCustomerWallet1.getUserId(), new Money(Currency.JPY, 1000), new HashMap<>()));
    Assertions.assertThat(p2PResponseDTO.getSenderPrepaidAmount()).isEqualTo(800);
    Assertions.assertThat(p2PResponseDTO.getSenderEmoneyAmount()).isEqualTo(200);
    Assertions.assertThat(p2PResponseDTO.getReceiverPrepaidAmount()).isEqualTo(800);
    Assertions.assertThat(p2PResponseDTO.getReceiverEmoneyAmount()).isEqualTo(200);
  }

  @Test
  public void expireConfirmAcceptWithMerchantCampaign() throws InterruptedException {
    BlockingQueue<Message<?>> queue = messageCollector.forChannel(expiredCashback.output());
    String testCampaign = "test_campaign";
    Date expiryDate = DateUtils.dateFromDays("3");
    Map<String, String> metadata = new HashMap<>();
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    createAndFundOneMerchantCampaign(merchantId, testCampaign);
    performMerchantCashbackDeposit(merchantId, testCampaign, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    List<TransactionV2DTO> pids = walletService.expireCashbackPrepare(new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>()));

    customerWalletApplicationService.acceptCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pids.get(0).getPid()), new HashMap<>()));
    assertThat(queue.size()).isEqualTo(1);

    Message<?> message1 = queue.take();
    assertThat(message1.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(String.format("%s-%s", merchantId, testCampaign));
    ConfirmOrCancelDTO confirmOrCancelDTO = (ConfirmOrCancelDTO) ((EventPayload) message1.getPayload()).getData();
    assertThat(confirmOrCancelDTO.getPid()).isEqualTo(Long.parseLong(pids.get(0).getPid()));
  }

  @Test
  public void expireConfirmAcceptWithSystemCampaign() throws InterruptedException {
    BlockingQueue<Message<?>> queue = messageCollector.forChannel(expiredCashback.output());
    String testCampaign = "test_campaign";
    Date expiryDate = DateUtils.dateFromDays("3");
    Map<String, String> metadata = new HashMap<>();
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    createAndFundOneSystemCampaign(testCampaign);
    performSystemCashbackDeposit(testCampaign, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    List<TransactionV2DTO> pids = walletService.expireCashbackPrepare(new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>()));

    customerWalletApplicationService.acceptCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pids.get(0).getPid()), new HashMap<>()));
    assertThat(queue.size()).isEqualTo(1);

    Message<?> message1 = queue.take();
    assertThat(message1.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(testCampaign);
    ConfirmOrCancelDTO confirmOrCancelDTO = (ConfirmOrCancelDTO) ((EventPayload) message1.getPayload()).getData();
    assertThat(confirmOrCancelDTO.getPid()).isEqualTo(Long.parseLong(pids.get(0).getPid()));
  }

  @Test
  public void expireConfirmAcceptWithSystemExpired() throws InterruptedException {
    BlockingQueue<Message<?>> queue = messageCollector.forChannel(expiredCashback.output());
    String testCampaign = "test_campaign";
    Date expiryDate = DateUtils.dateFromDays("3");
    Map<String, String> metadata = new HashMap<>();
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    createAndFundOneSystemCampaign(testCampaign);
    Money amount = new Money(Currency.JPY, 150);
    performSystemCashbackDeposit(testCampaign, AccountType.CASHBACK_EXPIRABLE, amount, metadata);
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, new HashMap<>()));
    walletService.capture(new CustomerCaptureDTO(Collections.singletonList(pid), new HashMap<>()));
    walletService.refund(new MerchantRefundDTO(merchantWallet.getUserId(), amount, new Money(0), pid, new HashMap<>()));
    Date refundedExpiryDate = DateUtils.getStartOfJstDay(DateUtils.dateFromDays("60"));

    List<TransactionV2DTO> pids = walletService.expireCashbackPrepare(new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(refundedExpiryDate), new HashMap<>()));

    customerWalletApplicationService.acceptCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pids.get(0).getPid()), new HashMap<>()));
    assertThat(queue.size()).isEqualTo(1);

    Message<?> message1 = queue.take();
    assertThat(message1.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo("SYSTEM-EXPIRED");
    ConfirmOrCancelDTO confirmOrCancelDTO = (ConfirmOrCancelDTO) ((EventPayload) message1.getPayload()).getData();
    assertThat(confirmOrCancelDTO.getPid()).isEqualTo(Long.parseLong(pids.get(0).getPid()));
  }

  @Test
  public void expireConfirmAcceptWithSameIdemKeyRequestIsAlreadyProcessed() throws InterruptedException {
    BlockingQueue<Message<?>> expiredCashbackQueue = messageCollector.forChannel(expiredCashback.output());
    BlockingQueue<Message<?>> expireConfirmCompletionQueue = messageCollector.forChannel(expireConfirmCompletion.output());
    String testCampaign = "test_campaign";
    Date expiryDate = DateUtils.dateFromDays("3");
    Map<String, String> metadata = new HashMap<>();
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    createAndFundOneSystemCampaign(testCampaign);
    performSystemCashbackDeposit(testCampaign, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    List<TransactionV2DTO> transactionV2DTOList = walletService.expireCashbackPrepare(new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>()));
    String pid = transactionV2DTOList.get(0).getPid();
    customerWalletApplicationService.processCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));
    customerWalletApplicationService.publishSuccessfulProcessingOfExpireConfirm("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));
    Optional<String> idem = idempotencyService.completedResult("1", String.class);
    assertThat(idem.isPresent()).isEqualTo(true);
    assertThat(expireConfirmCompletionQueue.size()).isEqualTo(1);
    Message<?> completionMessage = expireConfirmCompletionQueue.take();
    assertThat(completionMessage.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(pid);

    // accept with same idemKey which request is already processed should just publish to expireConfirmCompletion not expiredCashback.
    customerWalletApplicationService.acceptCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));
    assertThat(expiredCashbackQueue.size()).isEqualTo(0);
    assertThat(expireConfirmCompletionQueue.size()).isEqualTo(1);
    completionMessage = expireConfirmCompletionQueue.take();
    assertThat(completionMessage.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(pid);
  }

  @Test
  public void expireConfirmProcessShouldProcessThenPublish() throws InterruptedException {
    BlockingQueue<Message<?>> expireConfirmCompletionQueue = messageCollector.forChannel(expireConfirmCompletion.output());
    String testCampaign = "test_campaign";
    Date expiryDate = DateUtils.dateFromDays("3");
    Map<String, String> metadata = new HashMap<>();
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    createAndFundOneSystemCampaign(testCampaign);
    performSystemCashbackDeposit(testCampaign, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    List<TransactionV2DTO> transactionV2DTOList = walletService.expireCashbackPrepare(new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>()));
    String pid = transactionV2DTOList.get(0).getPid();
    customerWalletApplicationService.processCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));
    customerWalletApplicationService.publishSuccessfulProcessingOfExpireConfirm("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));

    Optional<String> idem = idempotencyService.completedResult("1", String.class);
    assertThat(idem.isPresent()).isEqualTo(true);
    assertThat(expireConfirmCompletionQueue.size()).isEqualTo(1);
    Message<?> message = expireConfirmCompletionQueue.take();
    assertThat(message.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo(pid);
    assertThat(((EventPayload)message.getPayload()).getHeader().getIdemKey()).isEqualTo("1");
    ConfirmOrCancelDTO confirmOrCancelDTO = (ConfirmOrCancelDTO) ((EventPayload) message.getPayload()).getData();
    assertThat(confirmOrCancelDTO.getPid()).isEqualTo(Long.parseLong(pid));
  }

  @Test(expected = IdempotentException.class)
  public void expireConfirmProcessWithAlreadyProcessedIdemShouldFailIdempotent() {
    String testCampaign = "test_campaign";
    Date expiryDate = DateUtils.dateFromDays("3");
    Map<String, String> metadata = new HashMap<>();
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    createAndFundOneSystemCampaign(testCampaign);
    performSystemCashbackDeposit(testCampaign, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    List<TransactionV2DTO> transactionV2DTOList = walletService.expireCashbackPrepare(new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>()));
    String pid = transactionV2DTOList.get(0).getPid();
    customerWalletApplicationService.processCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));
    customerWalletApplicationService.processCashbackExpireConfirmRequest("1", new ConfirmOrCancelDTO(Long.parseLong(pid), new HashMap<>()));
  }

  private void fundingKycCustomerWallet() {
    Account source = walletFactory.rebuildExternalWallet().source();

    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet.cashback(),
        new Money(Currency.JPY, 1000L),
        new Money(Currency.JPY, 1000L),
        null);
    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet.prepaid(),
        new Money(Currency.JPY, 800L),
        new Money(Currency.JPY, 800L),
        null);
    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet.emoney().get(),
        new Money(Currency.JPY, 500L),
        new Money(Currency.JPY, 500L),
        null);

    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet1.cashback(),
        new Money(Currency.JPY, 1000L),
        new Money(Currency.JPY, 1000L),
        null);
    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet1.prepaid(),
        new Money(Currency.JPY, 800L),
        new Money(Currency.JPY, 800L),
        null);
    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet1.emoney().get(),
        new Money(Currency.JPY, 500L),
        new Money(Currency.JPY, 500L),
        null);
  }
  
  private List<Long> toPids(long pid){
    List<Long> pids = new LinkedList();
    pids.add(pid);
    return pids;
  }

  private long performMerchantCashbackDeposit(String merchant, String campaignToken, AccountType type, Money cashback, Map<String, String> metadata) {

    updateMerhandDepositMetadata(metadata);

    MerchantCampaignCashbackDepositDTO request = new MerchantCampaignCashbackDepositDTO();
    request.merchantWalletOwnerId = merchant;
    request.customer_wallet_owner_id = customerWallet.getUserId();
    request.campaign_token = campaignToken;
    request.amount =  cashback;
    request.setAccountType(type.name());
    request.metadata = metadata;

    return Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(request).getPid());
  }

  private long performSystemCashbackDeposit(String campaignToken, AccountType type, Money cashback, Map<String, String> metadata) {

    updateSystemDepositMetadata(metadata);

    CustomerCashbackV3DTO request = new CustomerCashbackV3DTO();
    request.customer_wallet_owner_id = customerWallet.getUserId();
    request.campaign_token = campaignToken;
    request.amount =  cashback;
    request.setAccountType(type.name());
    request.metadata = metadata;

    return walletService.cashbackV3(request).getPid();
  }

  private void createAndFundOneMerchantCampaign(String merchant, String campaignToken) {
    walletService.createMerchantCampaignAccount((new CreateMerchantCampaignAccountDTO(merchant, campaignToken,null)));
    Money campaignFund = new Money(Currency.JPY, 10000);
    MerchantCampaignTopupDTO merchantCampaignTopupDTO = new MerchantCampaignTopupDTO(campaignToken, merchant, campaignFund, null );
    walletService.merchantCampaignAccountFunding((merchantCampaignTopupDTO));
  }

  private void createAndFundOneSystemCampaign(String campaignToken) {
    walletService.createCampaignAccount((new CreateCampaignAccountDTO(campaignToken, null)));
    Money campaignFund = new Money(Currency.JPY, 10000);
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, campaignFund);
    walletService.topupCampaignAccount((topupCampaignAccountDTO));
  }

  private Map<String, String> updateMerhandDepositMetadata(Map<String, String> metadata) {

    metadata.put("accepted_at", DateUtils.getNow());
    metadata.put("processed_at", DateUtils.getNow());
    metadata.put("merchant_name", "PayPay Store");

    return metadata;
  }

  private Map<String, String> updateSystemDepositMetadata(Map<String, String> metadata) {

    metadata.put("accepted_at", DateUtils.getNow());
    metadata.put("processed_at", DateUtils.getNow());

    return metadata;
  }
}