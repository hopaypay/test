package com.paytm.paylite.wallet.domain.wallet;

import com.paytm.paylite.wallet.controller.dto.request.BatchRequestElement;
import com.paytm.paylite.wallet.controller.dto.request.CampaignDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.CampaignTopupDTO;
import com.paytm.paylite.wallet.controller.dto.request.CampaignTopupReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.ConfirmOrCancelDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateCustomerEmoneyAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateCustomerWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantTopupAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerAuthorizeDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCaptureDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackV2DTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackV3DTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerGiftcardTopupPrepare;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PConfirmDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerTopupPrepare;
import com.paytm.paylite.wallet.controller.dto.request.MerchantBatchReleaseDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantCampaignCashbackDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantCampaignTopupDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantPayableFundingDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantPayoutPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantRefundDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantReleaseDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantTopupFundingDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantTopupPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.PrepareCustomerCashbackExpiryDTO;
import com.paytm.paylite.wallet.controller.dto.request.RevertDTO;
import com.paytm.paylite.wallet.controller.dto.request.ThirdPartyPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.CreateCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.TopupCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.UpdateCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.response.BatchResponseElement;
import com.paytm.paylite.wallet.controller.dto.response.MerchantCashbackReverseResponseDTO;
import com.paytm.paylite.wallet.controller.dto.response.TransactionDTO;
import com.paytm.paylite.wallet.controller.dto.response.TransactionV2DTO;
import com.paytm.paylite.wallet.controller.dto.response.admin.BestEffortChargeDTO;
import com.paytm.paylite.wallet.controller.exception.AccountNotFoundException;
import com.paytm.paylite.wallet.controller.exception.AccountTypeNotAllowedException;
import com.paytm.paylite.wallet.controller.exception.AlreadyCapturedException;
import com.paytm.paylite.wallet.controller.exception.AlreadyExpiredException;
import com.paytm.paylite.wallet.controller.exception.AlreadyRevertedException;
import com.paytm.paylite.wallet.controller.exception.BalanceLimitException;
import com.paytm.paylite.wallet.controller.exception.BudgetMismatchException;
import com.paytm.paylite.wallet.controller.exception.CreditNotAllowedException;
import com.paytm.paylite.wallet.controller.exception.DebitNotAllowedException;
import com.paytm.paylite.wallet.controller.exception.ExpiredCashbackDestinationTypeNotAllowedException;
import com.paytm.paylite.wallet.controller.exception.InvalidCashbackRequestException;
import com.paytm.paylite.wallet.controller.exception.InvalidMetadataException;
import com.paytm.paylite.wallet.controller.exception.MacroTxnStateMismatchException;
import com.paytm.paylite.wallet.controller.exception.NoSuchMacroTxnException;
import com.paytm.paylite.wallet.controller.exception.NotEnoughMoneyException;
import com.paytm.paylite.wallet.controller.exception.P2PSameWalletException;
import com.paytm.paylite.wallet.controller.exception.P2PWrongReceiverWalletException;
import com.paytm.paylite.wallet.controller.exception.TransactionLimitException;
import com.paytm.paylite.wallet.controller.exception.WalletNotFoundException;
import com.paytm.paylite.wallet.controller.exception.handler.TransactionNotFoundException;
import com.paytm.paylite.wallet.domain.BatchControl;
import com.paytm.paylite.wallet.domain.MetaKeys;
import com.paytm.paylite.wallet.domain.wallet.account.Account;
import com.paytm.paylite.wallet.domain.wallet.account.AccountRepository;
import com.paytm.paylite.wallet.domain.wallet.account.AccountType;
import com.paytm.paylite.wallet.domain.wallet.account.CashbackExpiry;
import com.paytm.paylite.wallet.domain.wallet.account.CashbackExpiryRepository;
import com.paytm.paylite.wallet.domain.wallet.account.CashbackExpiryStatus;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransaction;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransactionRepository;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransactionType;
import com.paytm.paylite.wallet.domain.wallet.account.ExpiredCashbackDestinationType;
import com.paytm.paylite.wallet.domain.wallet.account.MacroTransaction;
import com.paytm.paylite.wallet.domain.wallet.account.Transaction;
import com.paytm.paylite.wallet.domain.wallet.account.TransactionRepository;
import com.paytm.paylite.wallet.domain.wallet.account.expiable.ExpirableMoney;
import com.paytm.paylite.wallet.domain.wallet.account.expiable.ExpirableMoneyService;
import com.paytm.paylite.wallet.domain.wallet.account.monetary.Currency;
import com.paytm.paylite.wallet.domain.wallet.account.monetary.Money;
import com.paytm.paylite.wallet.infrastructure.repository.BatchControlRepository;
import com.paytm.paylite.wallet.infrastructure.uidgenerator.UuidGenerator;
import com.paytm.paylite.wallet.infrastructure.utils.DateUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.CASHBACK;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.CASHBACK_EXPIRABLE;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.EMONEY;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.PAYABLE;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.PAYMENT;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.PREPAID;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.SOURCE;
import static com.paytm.paylite.wallet.domain.wallet.account.AccountType.TOPUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "wallet.transaction.p2p.maximum=100000",
        "wallet.transaction.p2p.minimum=2",
        "wallet.transaction.giftcard.prepaid.minimum=100",
        "wallet.transaction.customer.payout.minimumTransaction:100",
        "wallet.transaction.customer.payout.maximumTransaction:500000",
        "wallet.daily.max=99990",
        "wallet.monthly.max=1000000"
})
public class WalletServiceTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired private WalletService walletService;
  @Autowired private WalletFactory walletFactory;
  @Autowired private TransactionRepository transactionRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private DerivedTransactionRepository derivedTransactionRepository;
  @Autowired private BatchControlRepository batchControlRepository;
  @Autowired private CashbackExpiryRepository cashbackExpiryRepository;
  @Autowired private ExpirableMoneyService expirableMoneyService;

  private CustomerWallet customerWallet;
  private CustomerWallet customerWallet1;
  private MerchantWallet merchantWallet;
  private String nonKycCustomerId = "nonkyc";
  private String nonKycCustomerId1 = "nonkyc1";
  private String merchantId = "merchant";
  private String merchantName = "PayPay Store";
  private String orderId = "03333333333333333333";
  private Money depositAmount = new Money(Currency.JPY, 10);
  private Money amount = new Money(Currency.JPY, 1);
  private Map<String, String> metadata = new HashMap<>();
  private Map<String, String> merchantMeta = new HashMap<>();
  private String campaignToken = "123";

  @Value("${wallet.transaction.customer.payout.minimumTransaction}")
  private long customerPayoutMinimumTransaction;
  @Value("${wallet.transaction.customer.payout.maximumTransaction}")
  private long customerPayoutMaximumTransaction;

  CustomerWallet kycCustomerWallet;
  CustomerWallet kycCustomerWallet1;

  private MerchantWallet merchantWalletWithOneCampaign;
  private MerchantWallet merchantWalletWithTwoCampaign;
  private String merchantIdWithOneCampaign = "merchant_with_1_campaign";
  private String merchantIdWithTwoCampaign = "merchant_with_2_campaign";
  private String merchantCampaignNotes = "Merchant Campaign";
  private String systemCampaignNotes = "System Campaign";

  @Before
  public void setUp() {
    Account source = walletFactory.rebuildExternalWallet().source();
    Account budget = walletFactory.rebuildSystemWallet().budget();
    source.transfer(
        UuidGenerator.getUID(),
        budget,
        new Money(Currency.JPY, 10000),
        new Money(Currency.JPY, 10000),
        null);
    customerWallet = (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(nonKycCustomerId));
    customerWallet1 = (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(nonKycCustomerId1));
    merchantWallet = (MerchantWallet) walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantId));
    merchantWalletWithOneCampaign = walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantIdWithOneCampaign));
    merchantWalletWithTwoCampaign = walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantIdWithTwoCampaign));

    kycCustomerWallet =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    kycCustomerWallet.createAccount(
        new Account(kycCustomerWallet, AccountType.EMONEY, Currency.JPY, true, null));
    kycCustomerWallet1 =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    kycCustomerWallet1.createAccount(
        new Account(kycCustomerWallet1, AccountType.EMONEY, Currency.JPY, true, null));

    metadata.put("key", "value");
    merchantMeta.put("payment_method_type", "MERCHANT");
  }

  @Test
  public void cannotCreateWalletWithSameUserId() {
    walletService.createCustomerWallet(new CreateCustomerWalletDTO("123"));
    Assertions.assertThatThrownBy(
            () -> {
              walletService.createMerchantWallet(new CreateMerchantWalletDTO("123"));
            }).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  public void customerDeposit() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    long beforeP = customerWallet.prepaid().balance().getCents();
    long beforeE = externalWallet.source().balance().getCents();

    long pid = walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    assertThat(customerWallet.prepaid().balance().getCents() - beforeP).isEqualTo(depositAmount.getCents());
    assertThat(beforeE - externalWallet.source().balance().getCents()).isEqualTo(0); //no update to source
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(1);
    assertThat(transactions.get(0).getSrcUserId()).isEqualTo(externalWallet.getUserId());
    assertThat(transactions.get(0).getDstUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(transactions.get(0).getSrcBalance()).isEqualTo(0);
    assertThat(transactions.get(0).getDstBalance()).isEqualTo(customerWallet.prepaid().balance().getCents());
    assertThat(transactions.get(0).getSrcCurrency()).isEqualTo(Currency.JPY);
    assertThat(transactions.get(0).getSrcAmount()).isEqualTo(depositAmount.getCents());
    assertThat(transactions.get(0).getDstAmount()).isEqualTo(depositAmount.getCents());
    assertThat(transactions.get(0).getMetadata()).isNotNull();
    assertThat(transactions.get(0).getMetadataByKey("key")).isEqualTo("value");
  }

  @Test(expected = BalanceLimitException.class)
  public void customerDepositBeyondLimit() {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), new Money(Currency.JPY, 1000001), metadata));
  }

  @Test
  public void prepare() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    Account source = externalWallet.source();
    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    Account payment = customerWallet.payment();

    long sourceBefore = source.balance().getCents();
    long paymentBefore = payment.balance().getCents();
    long pid = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));

    Assert.assertEquals(0, sourceBefore - source.balance().getCents());
    Assert.assertEquals(paymentBefore, payment.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(1);

    Transaction first = transactions.get(0);

    assertThat(first.getSrcUserId()).isEqualTo(externalWallet.getUserId());
    assertThat(first.getDstUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(first.getSrcAccountUuid())
            .isEqualTo(source.getId());
    assertThat(first.getDstAccountUuid())
            .isEqualTo(customerWallet.payment().getId());
    assertThat(first.getSrcBalance()).isEqualTo(0);
    assertThat(first.getDstBalance()).isEqualTo(0);
    assertThat(first.getSrcCurrency()).isEqualTo(Currency.JPY);
    assertThat(first.getDstAmount()).isEqualTo(amount.getCents());
    assertThat(first.getSrcAmount()).isEqualTo(amount.getCents());
    assertThat(first.getMetadata()).isNotNull();
    assertThat(first.getMetadataByKey("key")).isEqualTo("value");
    assertThat(first.getMetadataByKey("dst_wallet_owner_id"))
        .isEqualTo(String.valueOf(merchantWallet.getUserId()));
  }

  @Test
  public void authorize() {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    long paymentBalance = customerWallet.payment().balance().getCents();
    long prepaidBalance = customerWallet.prepaid().balance().getCents();

    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepaidBalance - amount.getCents());
    assertThat(customerWallet.payment().balance().getCents()).isEqualTo(paymentBalance);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(1);
    assertThat(transactions.get(0).getSrcUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(transactions.get(0).getSrcUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(transactions.get(0).getSrcAccountUuid())
        .isEqualTo(customerWallet.prepaid().getId());
    assertThat(transactions.get(0).getDstAccountUuid())
        .isEqualTo(customerWallet.payment().getId());
    assertThat(transactions.get(0).getSrcBalance()).isEqualTo(9);
    assertThat(transactions.get(0).getDstBalance()).isEqualTo(9);
    assertThat(transactions.get(0).getSrcCurrency()).isEqualTo(Currency.JPY);
    assertThat(transactions.get(0).getDstAmount()).isEqualTo(1);
    assertThat(transactions.get(0).getSrcAmount()).isEqualTo(1);
    assertThat(transactions.get(0).getMetadata()).isNotNull();
    assertThat(transactions.get(0).getMetadataByKey("key")).isEqualTo("value");
    assertThat(transactions.get(0).getMetadataByKey("dst_wallet_owner_id"))
        .isEqualTo(String.valueOf(merchantWallet.getUserId()));
  }

  @Test
  public void authorizeFailedForMaximum() {
    expectedException.expect(TransactionLimitException.class);
    expectedException.expectMessage("payment maximum: 500000");

    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), new Money(Currency.JPY, 1000000), metadata));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(1000000);

    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 500001L), metadata));
  }


  @Test // auth twice and capture once
  public void captureSingleAuth() {

    long paymentBalance = customerWallet.payment().balance().getCents();
    long payableBalance = merchantWallet.payable().balance().getCents();

    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));

    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));

    long firstChargeBalance = depositAmount.minus(amount).getCents();
    assertThat(customerWallet.getTotalBalance()).isEqualTo(firstChargeBalance);

    metadata.put("request_path", "/v1/customer/payment/authorize");
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));

    long secondChargeBalance = firstChargeBalance - amount.getCents();
    assertThat(customerWallet.getTotalBalance()).isEqualTo(secondChargeBalance);
    assertThat(customerWallet.payment().balance().getCents()).isEqualTo(paymentBalance);

    Map<String, String> captureMetadata = new HashMap<>();
    captureMetadata.put("request_path", "/v1/customer/payment/capture");
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), captureMetadata));

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    MerchantWallet merchantWallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantId);
    assertThat(customerWallet.payment().balance().getCents()).isEqualTo(paymentBalance);
    assertThat(customerWallet.getTotalBalance()).isEqualTo(secondChargeBalance);
    assertThat(merchantWallet.payable().balance().getCents()).isEqualTo(payableBalance + amount.getCents());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(2);
    Transaction first = transactions.get(0);
    assertThat(first.getMetadataByKey("request_path")).isEqualTo("/v1/customer/payment/authorize");

    Transaction last = transactions.get(transactions.size() - 1);
    assertThat(last.getMetadataByKey("request_path")).isEqualTo("/v1/customer/payment/capture");
    assertThat(last.getSrcUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(last.getSrcUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(last.getDstUserId()).isEqualTo(merchantWallet.getUserId());
    assertThat(last.getSrcAccountUuid()).isEqualTo(customerWallet.payment().getId());
    assertThat(last.getDstAccountUuid()).isEqualTo(merchantWallet.payable().getId());
    assertThat(last.getSrcBalance()).isEqualTo(secondChargeBalance);
    assertThat(last.getDstBalance()).isEqualTo(merchantWallet.getTotalBalance());
    assertThat(last.getSrcCurrency()).isEqualTo(Currency.JPY);
    assertThat(last.getDstAmount()).isEqualTo(amount.getCents());
    assertThat(last.getSrcAmount()).isEqualTo(amount.getCents());
  }

  @Test
  public void captureMultiAuth() {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    long paymentBalance = customerWallet.payment().balance().getCents();
    long prepaidBalance = customerWallet.prepaid().balance().getCents();
    long merchantBalance = merchantWallet.getTotalBalance();

    long pid1 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    long pid2 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    long customerBalance = customerWallet.getTotalBalance();

    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepaidBalance - 1);
    assertThat(customerWallet.payment().balance().getCents()).isEqualTo(paymentBalance);
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid1, pid2), null));
    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    MerchantWallet merchantWallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantId);

    assertThat(customerWallet.payment().balance().getCents()).isEqualTo(paymentBalance);
    assertThat(merchantWallet.payable().balance().getCents()).isEqualTo(amount.add(amount).getCents());

    List<Transaction> prepare = transactionRepository.transactionsOfPid(pid1);
    assertThat(prepare.size()).isEqualTo(2);
    Transaction last = prepare.get(prepare.size() - 1);
    assertThat(last.getSrcAccountUuid())
        .isEqualTo(customerWallet.payment().getId());
    assertThat(last.getDstAccountUuid())
        .isEqualTo(merchantWallet.payable().getId());
    assertThat(last.getSrcBalance()).isEqualTo(customerBalance);
    assertThat(last.getDstBalance()).isEqualTo(merchantBalance + amount.getCents());

    List<Transaction> authorize = transactionRepository.transactionsOfPid(pid2);
    assertThat(authorize.size()).isEqualTo(2);
    last = authorize.get(prepare.size() - 1);
    assertThat(last.getSrcUserId()).isEqualTo(customerWallet.getUserId());
    assertThat(last.getDstUserId()).isEqualTo(merchantWallet.getUserId());
    assertThat(last.getSrcAccountUuid())
        .isEqualTo(customerWallet.payment().getId());
    assertThat(last.getDstAccountUuid())
        .isEqualTo(merchantWallet.payable().getId());
    assertThat(last.getSrcBalance()).isEqualTo(customerBalance);
    assertThat(last.getDstBalance()).isEqualTo(merchantBalance + amount.add(amount).getCents());
    assertThat(last.getSrcCurrency()).isEqualTo(Currency.JPY);
    assertThat(last.getDstAmount()).isEqualTo(amount.getCents());
    assertThat(last.getSrcAmount()).isEqualTo(amount.getCents());
  }

  @Test(expected = AlreadyCapturedException.class)
  public void alreadyCaptured() {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(8);
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), null)); // double captured
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void reverseThenCapture() {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(8);
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), null)); // state error
  }

  @Test
  public void reverseAuthorizedWithNonKycUser() {
    long beforeP = customerWallet.prepaid().balance().getCents();
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());

    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(beforeP + depositAmount.getCents() - amount.getCents());
//    Assertions.assertThat(customerWallet.payment().balance().getCents()).isEqualTo(amount.getCents());

    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));

    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());
//    Assertions.assertThat(customerWallet.payment().balance().getCents()).isEqualTo(0);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(2);
    Transaction first = transactions.get(0);
    assertThat(first.getSrcAccountUuid()).isEqualTo(customerWallet.prepaid().getId());
    assertThat(first.getDstAccountUuid()).isEqualTo(customerWallet.payment().getId());

    Transaction last = transactions.get(1);
    assertThat(last.getSrcAccountUuid()).isEqualTo(customerWallet.payment().getId());
    assertThat(last.getDstAccountUuid()).isEqualTo(customerWallet.prepaid().getId());
    assertThat(last.getSrcBalance()).isEqualTo(depositAmount.getCents());
    assertThat(last.getDstBalance()).isEqualTo(depositAmount.getCents());

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(pid);
    assertThat(derivedTransactions.size()).isEqualTo(1);
    assertThat(derivedTransactions.get(0).getDerivedPid()).isEqualTo(pid);
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);

  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Test
  public void reverseAuthorizedWithKycUser() {
    Account emoney = kycCustomerWallet.emoney().get();
    Account payment = kycCustomerWallet.payment();

    // deposit
    long beforeP = emoney.balance().getCents();
    long beforePaymentBalance = kycCustomerWallet.getTotalBalance();
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), depositAmount, metadata));
    assertThat(emoney.balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());
    // authorize
    long pid = walletService.authorize(new CustomerAuthorizeDTO(kycCustomerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    assertThat(emoney.balance().getCents()).isEqualTo(beforeP + depositAmount.getCents() - amount.getCents());

    // reverse
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));

    // verify balance
    assertThat(emoney.balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());

    // verify transaction record
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions).extracting("srcUserId","srcAccountUuid","srcAmount","srcBalance","srcAccountType",
        "dstUserId","dstAccountUuid","dstAmount", "dstBalance", "dstAccountType").containsExactly(
        tuple(kycCustomerWallet.getUserId(), emoney.getId(), amount.getCents(), beforePaymentBalance + depositAmount.getCents() - amount.getCents(), EMONEY,
            kycCustomerWallet.getUserId(), payment.getId(), amount.getCents(), beforePaymentBalance + depositAmount.getCents() - amount.getCents(), PAYMENT),
        tuple(kycCustomerWallet.getUserId(), payment.getId(), amount.getCents(), beforePaymentBalance + depositAmount.getCents(), PAYMENT,
            kycCustomerWallet.getUserId(), emoney.getId(), amount.getCents(), beforePaymentBalance + depositAmount.getCents(), EMONEY)
    );

    // verify derived transaction record
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(pid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type").containsExactly(
        tuple(pid, pid, DerivedTransactionType.REVERT)
    );
  }

  @Test
  public void reverseCapturedWithNonKycUser() {
    long beforeP = customerWallet.prepaid().balance().getCents();

    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));

    assertThat(customerWallet.prepaid().balance().getCents() - beforeP).isEqualTo(depositAmount.getCents());
//    Assertions.assertThat(customerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(merchantWallet.payable().balance().getCents()).isEqualTo(0);

    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(beforeP + depositAmount.getCents() - amount.getCents());

    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), null));
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    MerchantWallet merchantWallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantId);

    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());
//    Assertions.assertThat(customerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(merchantWallet.payable().balance().getCents()).isEqualTo(0);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(4);
  }

  @Test
  public void reverseCapturedWithKycUser() {
    Account emoney = kycCustomerWallet.emoney().get();
    Account payment = kycCustomerWallet.payment();
    Account payable = merchantWallet.payable();

    long originalCustomerBalance = kycCustomerWallet.getTotalBalance();
    long originalMerchantBalance = merchantWallet.getTotalBalance();

    // deposit
    long beforeP = emoney.balance().getCents();
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), depositAmount, metadata));
    assertThat(emoney.balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());
    // authorize
    long pid = walletService.authorize(new CustomerAuthorizeDTO(kycCustomerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    assertThat(emoney.balance().getCents()).isEqualTo(beforeP + depositAmount.getCents() - amount.getCents());
    // capture
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), null));

    // reverse
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));

    // verify balance
    assertThat(emoney.balance().getCents()).isEqualTo(beforeP + depositAmount.getCents());

    // verify transaction record
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions).extracting("srcUserId","srcAccountUuid","srcAmount","srcBalance","srcAccountType",
        "dstUserId","dstAccountUuid","dstAmount", "dstBalance", "dstAccountType").containsExactly(
        // authorize(emoney -> payment)
        tuple(kycCustomerWallet.getUserId(), emoney.getId(), amount.getCents(), originalCustomerBalance + depositAmount.getCents() - amount.getCents(), EMONEY,
            kycCustomerWallet.getUserId(), payment.getId(), amount.getCents(), originalCustomerBalance + depositAmount.getCents() - amount.getCents(), PAYMENT),
        // capture(payment -> payable)
        tuple(kycCustomerWallet.getUserId(), payment.getId(), amount.getCents(), originalCustomerBalance + depositAmount.getCents() - amount.getCents(), PAYMENT,
            merchantWallet.getUserId(), payable.getId(), amount.getCents(), originalMerchantBalance + amount.getCents(), PAYABLE),
        // capture reverse(payable -> payment)
        tuple(merchantWallet.getUserId(), payable.getId(), amount.getCents(), originalMerchantBalance, PAYABLE,
            kycCustomerWallet.getUserId(), payment.getId(), amount.getCents(), originalCustomerBalance + depositAmount.getCents() - amount.getCents(), PAYMENT),
        // authorize reverse(payment -> emoney)
        tuple(kycCustomerWallet.getUserId(), payment.getId(), amount.getCents(), originalCustomerBalance + depositAmount.getCents(), PAYMENT,
            kycCustomerWallet.getUserId(), emoney.getId(), amount.getCents(), originalCustomerBalance + depositAmount.getCents(), EMONEY)
    );

    // verify derived transaction record
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(pid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type").containsExactly(
        tuple(pid, pid, DerivedTransactionType.REVERT)
    );
  }

  @Test
  public void paymentReverseIncludeCashbackExpirable() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    Map<String, String> authorizeMeta = new HashMap<>();
    authorizeMeta.put(MetaKeys.ORDER_ID, orderId);
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(1500), authorizeMeta));

    Assert.assertEquals(0L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0L, cashback.balance().getCents().longValue());
    Assert.assertEquals(500L, prepaid.balance().getCents().longValue());

    Map<String, String> paymentReverseMeta = new HashMap<>();
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), paymentReverseMeta));

    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(6);
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.PAYMENT, 200L, 200L),
            tuple(AccountType.CASHBACK, AccountType.PAYMENT, 1000L, 1000L),
            tuple(AccountType.PREPAID, AccountType.PAYMENT, 300L, 300L),
            tuple(AccountType.PAYMENT, AccountType.PREPAID, 300L, 300L),
            tuple(AccountType.PAYMENT, AccountType.CASHBACK, 1000L, 1000L),
            tuple(AccountType.PAYMENT, AccountType.CASHBACK_EXPIRABLE, 200L, 200L)
        );
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(1).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(2).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(3).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(4).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(5).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_EXPIRED.name());

    CashbackExpiry cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(pid).get(0);
    assertThat(cashbackExpiry.getPid()).isEqualTo(pid);
    assertThat(cashbackExpiry.getCashback().getCents()).isEqualTo(200L);
    long dayDiff = (cashbackExpiry.getExpiryDate().getTime() - cashbackExpiry.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24);
    assertThat(dayDiff).isEqualTo(59L); // It should be as close as possible to 60L. (e.g. 59.99..)
    assertThat(cashbackExpiry.getStatus()).isEqualTo(CashbackExpiryStatus.COMPLETED);
    assertThat(cashbackExpiry.getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_EXPIRED.name());
    assertThat(cashbackExpiry.getMetadata().get(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(DateUtils.dateToString(cashbackExpiry.getExpiryDate())).isEqualTo(transactions.get(5).getMetadataByKey("expiry_date"));
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Test
  public void paymentReverseIncludeAllAccountWithKycUser() {
    // set up account
    funding(kycCustomerWallet, AccountType.CASHBACK_EXPIRABLE, 200);
    funding(kycCustomerWallet, AccountType.CASHBACK, 1000);
    funding(kycCustomerWallet, AccountType.PREPAID, 800);
    funding(kycCustomerWallet, EMONEY, 500);

    Account cashbackExpirable = kycCustomerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = kycCustomerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    Account emoney = kycCustomerWallet.findAccountByType(EMONEY).get();
    Account payment = kycCustomerWallet.findAccountByType(PAYMENT).get();

    long beforePaymentBalance = kycCustomerWallet.getTotalBalance();
    long paymentAmount = 2300L;

    // authorize
    long pid = walletService.authorize(new CustomerAuthorizeDTO(kycCustomerWallet.getUserId(), merchantWallet.getUserId(), new Money(paymentAmount), metadata));

    // verify balance
    long restAmount = kycCustomerWallet.getTotalBalance();
    assertThat(restAmount).isEqualTo(200L);
    assertThat(cashbackExpirable.balance().getCents()).isEqualTo(0L);
    assertThat(cashback.balance().getCents()).isEqualTo(0L);
    assertThat(prepaid.balance().getCents()).isEqualTo(0L);
    assertThat(emoney.balance().getCents()).isEqualTo(200L);

    // reverse
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));

    // verify balance
    assertThat(kycCustomerWallet.getTotalBalance()).isEqualTo(beforePaymentBalance);
    assertThat(cashbackExpirable.balance().getCents()).isEqualTo(200L);
    assertThat(cashback.balance().getCents()).isEqualTo(1000L);
    assertThat(prepaid.balance().getCents()).isEqualTo(800L);
    assertThat(emoney.balance().getCents()).isEqualTo(500L);

    // verify transaction record
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions).extracting("srcUserId", "srcAccountUuid", "srcAmount", "srcBalance", "srcAccountType",
        "dstUserId", "dstAccountUuid", "dstAmount", "dstBalance", "dstAccountType").containsExactly(
        // authorize
        // cashback_expirable -> payment
        tuple(kycCustomerWallet.getUserId(), cashbackExpirable.getId(), 200L, beforePaymentBalance - 200L, CASHBACK_EXPIRABLE,
            kycCustomerWallet.getUserId(), payment.getId(), 200L, beforePaymentBalance - 200L, PAYMENT),
        // cashback -> payment
        tuple(kycCustomerWallet.getUserId(), cashback.getId(), 1000L, (beforePaymentBalance - 200L) - 1000L, CASHBACK,
            kycCustomerWallet.getUserId(), payment.getId(), 1000L, (beforePaymentBalance - 200L) - 1000L, PAYMENT),
        // prepaid -> payment
        tuple(kycCustomerWallet.getUserId(), prepaid.getId(), 800L, ((beforePaymentBalance - 200L) - 1000L) - 800L, PREPAID,
            kycCustomerWallet.getUserId(), payment.getId(), 800L, ((beforePaymentBalance - 200L) - 1000L) - 800L, PAYMENT),
        // emoney -> payment
        tuple(kycCustomerWallet.getUserId(), emoney.getId(), 300L, (((beforePaymentBalance - 200L) - 1000L) - 800L) - 300L, EMONEY,
            kycCustomerWallet.getUserId(), payment.getId(), 300L, (((beforePaymentBalance - 200L) - 1000L) - 800L) - 300L, PAYMENT),
        // reverse
        // payment -> emoney
        tuple(kycCustomerWallet.getUserId(), payment.getId(), 300L, restAmount + 300L, PAYMENT,
            kycCustomerWallet.getUserId(), emoney.getId(), 300L, restAmount + 300L, EMONEY),
        // payment -> prepaid
        tuple(kycCustomerWallet.getUserId(), payment.getId(), 800L, (restAmount + 300L) + 800L, PAYMENT,
            kycCustomerWallet.getUserId(), prepaid.getId(), 800L, (restAmount + 300L) + 800L, PREPAID),
        // payment -> cashback
        tuple(kycCustomerWallet.getUserId(), payment.getId(), 1000L, ((restAmount + 300L) + 800L) + 1000L, PAYMENT,
            kycCustomerWallet.getUserId(), cashback.getId(), 1000L, ((restAmount + 300L) + 800L) + 1000L, CASHBACK),
        // payment -> cashback_expirable
        tuple(kycCustomerWallet.getUserId(), payment.getId(), 200L, (((restAmount + 300L) + 800L) + 1000L) + 200L, PAYMENT,
            kycCustomerWallet.getUserId(), cashbackExpirable.getId(), 200L, (((restAmount + 300L) + 800L) + 1000L) + 200L, CASHBACK_EXPIRABLE)
    );

    // verify derived transaction record
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(pid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type").containsExactly(
        tuple(pid, pid, DerivedTransactionType.REVERT)
    );
  }

  @Test(expected = AlreadyRevertedException.class)
  public void alreadyReverted() {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), depositAmount, metadata));
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, metadata));
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>()));
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(pid), new HashMap<>())); // double reverse
  }

  @Test
  public void merchantWalletShouldHaveAccounts() {
    Optional<Account> payout = merchantWallet.findAccountByType(AccountType.PAYOUT);
    Assert.assertEquals(true, payout.isPresent());
    Assert.assertEquals(false, payout.get().isVisible());
    Assert.assertEquals(0, payout.get().balance().getCents().longValue());

    Optional<Account> payable = merchantWallet.findAccountByType(AccountType.PAYABLE);
    Assert.assertEquals(true, payable.isPresent());
    Assert.assertEquals(true, payable.get().isVisible());
    Assert.assertEquals(0, payable.get().balance().getCents().longValue());

    Optional<Account> emoney = merchantWallet.findAccountByType(AccountType.EMONEY);
    Assert.assertEquals(true, emoney.isPresent());
    Assert.assertEquals(true, emoney.get().isVisible());
    Assert.assertEquals(0, emoney.get().balance().getCents().longValue());
  }

  @Test
  public void customerWalletShouldHaveAccounts() {

    Optional<Account> cashback = customerWallet.findAccountByType(AccountType.CASHBACK);
    Assert.assertEquals(true, cashback.isPresent());
    Assert.assertEquals(true, cashback.get().isVisible());
    Assert.assertEquals(0, cashback.get().balance().getCents().longValue());

    Optional<Account> prepaid = customerWallet.findAccountByType(AccountType.PREPAID);
    Assert.assertEquals(true, prepaid.isPresent());
    Assert.assertEquals(true, prepaid.get().isVisible());
    Assert.assertEquals(0, prepaid.get().balance().getCents().longValue());

    Optional<Account> payment = customerWallet.findAccountByType(AccountType.PAYMENT);
    Assert.assertEquals(true, payment.isPresent());
    Assert.assertEquals(false, payment.get().isVisible());
    Assert.assertEquals(0, payment.get().balance().getCents().longValue());

    Optional<Account> payout = customerWallet.findAccountByType(AccountType.PAYOUT);
    Assert.assertEquals(true, payout.isPresent());
    Assert.assertEquals(false, payout.get().isVisible());
    Assert.assertEquals(0, payout.get().balance().getCents().longValue());

    Optional<Account> incoming = customerWallet.findAccountByType(AccountType.INCOMING);
    Assert.assertEquals(true, incoming.isPresent());
    Assert.assertEquals(false, incoming.get().isVisible());
    Assert.assertEquals(0, incoming.get().balance().getCents().longValue());

    Optional<Account> cashbackPending = customerWallet.findAccountByType(AccountType.CASHBACK_PENDING);
    Assert.assertEquals(true, cashbackPending.isPresent());
    Assert.assertEquals(false, cashbackPending.get().isVisible());
    Assert.assertEquals(0, cashbackPending.get().balance().getCents().longValue());

    Optional<Account> P2PPending = customerWallet.findAccountByType(AccountType.P2P_PENDING);
    Assert.assertEquals(true, P2PPending.isPresent());
    Assert.assertEquals(false, P2PPending.get().isVisible());
    Assert.assertEquals(0, P2PPending.get().balance().getCents().longValue());

    Optional<Account> campaign = customerWallet.findAccountByType(AccountType.CAMPAIGN);
    Assert.assertEquals(true, campaign.isPresent());
    Assert.assertEquals(false, campaign.get().isVisible());
    Assert.assertEquals(0, campaign.get().balance().getCents().longValue());

    Optional<Account> cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE);
    Assert.assertEquals(true, cashbackExpirable.isPresent());
    Assert.assertEquals(true, cashbackExpirable.get().isVisible());
    Assert.assertEquals(0, cashbackExpirable.get().balance().getCents().longValue());

    Optional<Account> expired = customerWallet.findAccountByType(AccountType.EXPIRED);
    Assert.assertEquals(true, expired.isPresent());
    Assert.assertEquals(false, expired.get().isVisible());
    Assert.assertEquals(0, expired.get().balance().getCents().longValue());

  }

  @Test
  public void depositToCustomerWallet() {

    Account prepaid = customerWallet.prepaid();
    Assert.assertEquals(0L, prepaid.balance().getCents().longValue());
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), new Money(Currency.JPY, 8888), null));
    Assert.assertEquals(8888L, prepaid.balance().getCents().longValue());
  }

  @Test
  public void authChargeCashbackFirst() {

    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    long txnId =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 800),null));

    Assert.assertEquals(200L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    Assert.assertEquals(1, transactions.size());
    Map<String, String> meta = transactions.get(0).getMetadata();
    Assert.assertEquals(merchantWallet.getUserId(), meta.get("dst_wallet_owner_id"));
  }

  @Test
  public void authChargePrepaidSecond() {

    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    long txnId =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 1300),null));

    Assert.assertEquals(0L, cashback.balance().getCents().longValue());
    Assert.assertEquals(500L, prepaid.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    Assert.assertEquals(2, transactions.size());
    Map<String, String> meta = transactions.get(0).getMetadata();
    Assert.assertEquals(merchantWallet.getUserId(), meta.get("dst_wallet_owner_id"));
  }

  @Test
  public void authChargeCashbackExpirableFirst() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    long txnId =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 150),null));

    Assert.assertEquals(50L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    Assert.assertEquals(1, transactions.size());
    Map<String, String> meta = transactions.get(0).getMetadata();
    Assert.assertEquals(merchantWallet.getUserId(), meta.get("dst_wallet_owner_id"));
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.PAYMENT, 150L, 150L)
        );
  }

  @Test
  public void authChargeCashbackSecond() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    long txnId =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 500),null));

    Assert.assertEquals(0L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(700L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    Assert.assertEquals(2, transactions.size());
    Map<String, String> meta = transactions.get(0).getMetadata();
    Assert.assertEquals(merchantWallet.getUserId(), meta.get("dst_wallet_owner_id"));
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.PAYMENT, 200L, 200L),
            tuple(AccountType.CASHBACK, AccountType.PAYMENT, 300L, 300L)
        );
  }

  @Test
  public void authChargePrepaidThird() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    long txnId =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 1500),null));

    Assert.assertEquals(0L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0L, cashback.balance().getCents().longValue());
    Assert.assertEquals(500L, prepaid.balance().getCents().longValue());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    Assert.assertEquals(3, transactions.size());
    Map<String, String> meta = transactions.get(0).getMetadata();
    Assert.assertEquals(merchantWallet.getUserId(), meta.get("dst_wallet_owner_id"));
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.PAYMENT, 200L, 200L),
            tuple(AccountType.CASHBACK, AccountType.PAYMENT, 1000L, 1000L),
            tuple(AccountType.PREPAID, AccountType.PAYMENT, 300L, 300L)
        );
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void authNoEnoughFund() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 2100),null));
  }

  @Test(expected = TransactionLimitException.class)
  public void p2pKyc2NonKycOutOfLimitLessThanTwo() {
    fundingKycCustomerWallet();
    walletService.p2p(new CustomerP2PDTO(kycCustomerWallet.getUserId(), customerWallet.getUserId(), new Money(Currency.JPY, 1), null));
  }

  @Test
  public void p2pKyc2NonKyc() {
    fundingKycCustomerWallet();

    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    long before = prepaid.balance().getCents();
    walletService.p2p(new CustomerP2PDTO(kycCustomerWallet.getUserId(), customerWallet.getUserId(), new Money(Currency.JPY, 1300), new HashMap<>()));
    Assert.assertEquals(1300, prepaid.balance().getCents() - before);
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void p2pNonKyc2KycCannotUseCashback() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account prepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    long before = prepaid.balance().getCents();
    walletService.p2p(new CustomerP2PDTO(customerWallet.getUserId(), kycCustomerWallet.getUserId(), new Money(Currency.JPY, 1300), null));
  }

  @Test
  public void p2pNonKyc2KycSuccess() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account prepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    long before = prepaid.balance().getCents();
    walletService.p2p(new CustomerP2PDTO(customerWallet.getUserId(), kycCustomerWallet.getUserId(), new Money(Currency.JPY, 300), new HashMap<>()));
    Assert.assertEquals(300, prepaid.balance().getCents() - before);
  }

  @Test
  public void p2pNonKyc2KycSuccessThenRevert() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    long amount = 300;

    Account senderPrepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Account receiverPrepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();

    long beforeSP = senderPrepaid.balance().getCents();
    long before = receiverPrepaid.balance().getCents();

    MacroTransaction p2p = walletService.p2p(new CustomerP2PDTO(customerWallet.getUserId(), kycCustomerWallet.getUserId(), new Money(Currency.JPY, amount),  new HashMap<>()));

    Assertions.assertThat(senderPrepaid.balance().getCents()).isEqualTo(beforeSP - amount);
    Assertions.assertThat(receiverPrepaid.balance().getCents()).isEqualTo(before + amount);

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    walletService.p2pRevert(new RevertDTO(p2p.getPid(), meta));

    Assertions.assertThat(senderPrepaid.balance().getCents()).isEqualTo(beforeSP);
    Assertions.assertThat(receiverPrepaid.balance().getCents()).isEqualTo(before);
  }


  @Test(expected = P2PSameWalletException.class)
  public void p2pSelfFail() {
    fundingKycCustomerWallet();
    walletService.p2p(new CustomerP2PDTO(kycCustomerWallet.getUserId(), kycCustomerWallet.getUserId(), new Money(Currency.JPY, 1300), null));
  }

  @Test
  public void p2pKyc2KycSuccess() {
    fundingKycCustomerWallet();

    Account prepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    Account emoney = kycCustomerWallet.findAccountByType(AccountType.EMONEY).get();
    long before = prepaid.balance().getCents();
    long before2 = emoney.balance().getCents();
    walletService.p2p(new CustomerP2PDTO(kycCustomerWallet1.getUserId(), kycCustomerWallet.getUserId(), new Money(Currency.JPY, 1300), new HashMap<>()));
    Assert.assertEquals(800, prepaid.balance().getCents() - before);
    Assert.assertEquals(500, emoney.balance().getCents() - before2);
  }

  @Test
  public void p2pKyc2KycSuccessThenRevert() {
    fundingKycCustomerWallet();

    Account senderPrepaid = kycCustomerWallet1.findAccountByType(AccountType.PREPAID).get();
    Account senderEmoney = kycCustomerWallet1.findAccountByType(AccountType.EMONEY).get();
    Account receiverPrepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    Account receiverEmoney = kycCustomerWallet.findAccountByType(AccountType.EMONEY).get();

    long beforeSP = senderPrepaid.balance().getCents();
    long beforeSE = senderEmoney.balance().getCents();
    long before = receiverPrepaid.balance().getCents();
    long before2 = receiverEmoney.balance().getCents();

    MacroTransaction p2p = walletService.p2p(new CustomerP2PDTO(kycCustomerWallet1.getUserId(), kycCustomerWallet.getUserId(), new Money(Currency.JPY, 1300), new HashMap<>()));

    Assertions.assertThat(senderPrepaid.balance().getCents()).isEqualTo(beforeSP - 800);
    Assertions.assertThat(senderEmoney.balance().getCents()).isEqualTo(beforeSE - 500);
    Assertions.assertThat(receiverPrepaid.balance().getCents()).isEqualTo(before + 800);
    Assertions.assertThat(receiverEmoney.balance().getCents()).isEqualTo(before2 + 500);

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    walletService.p2pRevert(new RevertDTO(p2p.getPid(), meta));

    Assertions.assertThat(senderPrepaid.balance().getCents()).isEqualTo(beforeSP);
    Assertions.assertThat(senderEmoney.balance().getCents()).isEqualTo(beforeSE);
    Assertions.assertThat(receiverPrepaid.balance().getCents()).isEqualTo(before);
    Assertions.assertThat(receiverEmoney.balance().getCents()).isEqualTo(before2);
  }

  @Test(expected = TransactionLimitException.class)
  public void p2pPrepareKycOutOfLimitLessThanTwo() {
    fundingKycCustomerWallet();
    walletService.p2pPrepare(new CustomerP2PPrepareDTO(kycCustomerWallet.getUserId(), new Money(Currency.JPY, 1), null));
  }

  @Test
  public void p2pPrepareKyc() {
    fundingKycCustomerWallet();

    Account emoney = kycCustomerWallet.findAccountByType(AccountType.EMONEY).get();
    Account prepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    Account p2pPending = kycCustomerWallet.findAccountByType(AccountType.P2P_PENDING).get();
    long beforeEMoney = emoney.balance().getCents();
    long beforePrepaid = prepaid.balance().getCents();
    long beforeP2PPending = p2pPending.balance().getCents();
    walletService.p2pPrepare(new CustomerP2PPrepareDTO(kycCustomerWallet.getUserId(), new Money(Currency.JPY, 1300), null));
    Assert.assertEquals(500, beforeEMoney - emoney.balance().getCents());
    Assert.assertEquals(800, beforePrepaid - prepaid.balance().getCents());
    Assert.assertEquals(1300, p2pPending.balance().getCents()- beforeP2PPending);
  }

  @Test
  public void p2pPrepareNonKyc() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Account p2pPending = customerWallet.findAccountByType(AccountType.P2P_PENDING).get();
    long beforePrepaid = prepaid.balance().getCents();
    long beforeP2PPending = p2pPending.balance().getCents();
    walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    Assert.assertEquals(300, beforePrepaid - prepaid.balance().getCents());
    Assert.assertEquals(300, p2pPending.balance().getCents()- beforeP2PPending);
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void p2pPrepareNonKycCannotUseCashbackOrCashbackExpirable() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    long before = prepaid.balance().getCents();
    walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 1300), null));
  }

  @Test
  public void p2pPrepareShouldUseOnlyPrepaid(){

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    Money prepareAmount = new Money(Currency.JPY, 300);
    MacroTransaction t = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), prepareAmount, null));

    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(500L, prepaid.balance().getCents().longValue());
    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    Assert.assertEquals(1, transactions.size());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(AccountType.PREPAID, AccountType.P2P_PENDING, prepareAmount.getCents(), prepareAmount.getCents())
        );
  }

  @Test
  public void p2pConfirmNonKyc2NonKyc() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account senderPrepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Account receiverPrepaid = customerWallet1.findAccountByType(AccountType.PREPAID).get();
    long beforeSenderPrepaid = senderPrepaid.balance().getCents();
    long beforeReceiverPrepaid = receiverPrepaid.balance().getCents();
    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));

    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet1.getUserId(), null));
    Assert.assertEquals(300, beforeSenderPrepaid - senderPrepaid.balance().getCents());
    Assert.assertEquals(300, receiverPrepaid.balance().getCents() - beforeReceiverPrepaid);
  }

  @Test
  public void p2pConfirmNonKyc2NonKycSpecifiedReceiverInPrepare() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account senderPrepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Account receiverPrepaid = customerWallet1.findAccountByType(AccountType.PREPAID).get();
    long beforeSenderPrepaid = senderPrepaid.balance().getCents();
    long beforeReceiverPrepaid = receiverPrepaid.balance().getCents();
    Map<String, String> metadata = new HashMap<>();
    metadata.put("receiver_wallet_owner_id", customerWallet1.getUserId());
    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), metadata));

    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet1.getUserId(), null));
    Assert.assertEquals(300, beforeSenderPrepaid - senderPrepaid.balance().getCents());
    Assert.assertEquals(300, receiverPrepaid.balance().getCents() - beforeReceiverPrepaid);
  }

  @Test(expected = NoSuchMacroTxnException.class)
  public void p2pConfirmNonExistsPid() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(99999999999999L, customerWallet.getUserId(), null));
  }

  @Test(expected = P2PSameWalletException.class)
  public void p2pConfirmSameWallet() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet.getUserId(), null));
  }

  @Test(expected = P2PWrongReceiverWalletException.class)
  public void p2pConfirmWrongReceiver() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Map<String, String> metadata = new HashMap<>();
    metadata.put("receiver_wallet_owner_id", customerWallet1.getUserId());
    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), metadata));

    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), "differentUser", null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pConfirmAlreadyConfirmed() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));

    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet1.getUserId(), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet1.getUserId(), null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pConfirmAlreadyCanceled() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));

    walletService.p2pCancel(new ConfirmOrCancelDTO(macroTransaction.getPid(), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet1.getUserId(), null));
  }

  @Test
  public void p2pCancel() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account senderPrepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Account senderP2PPending = customerWallet.findAccountByType(AccountType.P2P_PENDING).get();

    Map<String, String> prepareMetadata = new HashMap<String, String>() {
      {
        put("receiver_wallet_owner_id", "12345");
        put("expiry_date", "2019-03-10T19:00:00:00.000");
        put("p2p_type", "on");
        put("p2p_phone_number", "*********");
      }
    };
    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), prepareMetadata));

    long beforeSenderPrepaid = senderPrepaid.balance().getCents();
    long beforeSenderP2PPending = senderP2PPending.balance().getCents();

    Map<String, String> cancelMetadata = new HashMap<String, String>() {
      {
        put("code", "p2p_declined");
      }
    };
    MacroTransaction canceledMacroTxn = walletService.p2pCancel(new ConfirmOrCancelDTO(preparedMacroTxn.getPid(), cancelMetadata));

    Assert.assertEquals(300, beforeSenderP2PPending - senderP2PPending.balance().getCents());
    Assert.assertEquals(300, senderPrepaid.balance().getCents() - beforeSenderPrepaid);

    Transaction last = canceledMacroTxn.getTransactions().get(canceledMacroTxn.getTransactions().size()-1);
    Assert.assertEquals(preparedMacroTxn.getPid(), last.getPid());
    Assert.assertEquals(AccountType.P2P_PENDING, last.getSrcAccountType());
    Assert.assertEquals(AccountType.PREPAID, last.getDstAccountType());
    Assert.assertEquals(prepareMetadata.get("expiry_date"), last.getMetadata().get("expiry_date"));
    Assert.assertEquals(prepareMetadata.get("p2p_type"), last.getMetadata().get("p2p_type"));
    Assert.assertEquals(prepareMetadata.get("p2p_phone_number"), last.getMetadata().get("p2p_phone_number"));
    Assert.assertEquals(cancelMetadata.get("code"), last.getMetadata().get("code"));
    Assert.assertEquals(prepareMetadata.get("receiver_wallet_owner_id"), last.getMetadata().get("receiver_wallet_owner_id"));
  }

  @Test
  public void p2pCancelFailedForPrepaidLimit() {
    expectedException.expect(BalanceLimitException.class);
    expectedException.expectMessage("exceeds 1,000,000 limit");

    // topup 800 yen
    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 800), null));

    // topup 500,000 yen x 2
    HashMap<String, String> meta = new HashMap<>();
    meta.put("payment_method_type", "BANK");
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, 500000L), meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));
    txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, 500000L), meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    // BalanceLimitException
    walletService.p2pCancel(new ConfirmOrCancelDTO(preparedMacroTxn.getPid(), null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pCancelAlreadyCanceled() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));

    walletService.p2pCancel(new ConfirmOrCancelDTO(macroTransaction.getPid(), null));
    walletService.p2pCancel(new ConfirmOrCancelDTO(macroTransaction.getPid(), null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pCancelAlreadyConfirmed() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction macroTransaction = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));

    walletService.p2pConfirm(new CustomerP2PConfirmDTO(macroTransaction.getPid(), customerWallet1.getUserId(), null));
    walletService.p2pCancel(new ConfirmOrCancelDTO(macroTransaction.getPid(), null));
  }

  @Test
  public void p2pRevertV2() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account senderPrepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Account receiverPrepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();

    long beforeSP = senderPrepaid.balance().getCents();
    long before = receiverPrepaid.balance().getCents();

    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(preparedMacroTxn.getPid(), customerWallet1.getUserId(), new HashMap<>()));
    walletService.p2pRevertV2(new RevertDTO(preparedMacroTxn.getPid(), new HashMap<>()));

    assertThat(senderPrepaid.balance().getCents()).isEqualTo(beforeSP);
    assertThat(receiverPrepaid.balance().getCents()).isEqualTo(before);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(preparedMacroTxn.getPid());
    assertThat(transactions).hasSize(2);

    final List<DerivedTransaction> derivedTransactions = derivedTransactionRepository
        .transactionsOfOriginalPid(preparedMacroTxn.getPid());
    assertThat(derivedTransactions).hasSize(1);
    assertThat(derivedTransactions.get(0).getOriginalPid()).isEqualTo(preparedMacroTxn.getPid());
    assertThat(derivedTransactions.get(0).getDerivedPid()).isNotEqualTo(preparedMacroTxn.getPid());
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);

    transactions = transactionRepository.transactionsOfPid(derivedTransactions.get(0).getDerivedPid());
    assertThat(transactions).hasSize(1);
    assertThat(transactions.get(0).getPid()).isNotEqualTo(preparedMacroTxn.getPid());
    assertThat(transactions.get(0).getSrcAccountType()).isEqualTo(AccountType.PREPAID);
    assertThat(transactions.get(0).getDstAccountType()).isEqualTo(AccountType.PREPAID);
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pPrepareThenRevertV2(){

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    walletService.p2pRevertV2(new RevertDTO(preparedMacroTxn.getPid(), null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pCancelThenRevertV2(){

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(preparedMacroTxn.getPid(), customerWallet1.getUserId(), null));
    walletService.p2pCancel(new ConfirmOrCancelDTO(preparedMacroTxn.getPid(), null));

    walletService.p2pRevertV2(new RevertDTO(preparedMacroTxn.getPid(), null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void p2pRevertV2ThenRevertV2(){

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(preparedMacroTxn.getPid(), customerWallet1.getUserId(), null));

    walletService.p2pRevertV2(new RevertDTO(preparedMacroTxn.getPid(), new HashMap<>()));
    walletService.p2pRevertV2(new RevertDTO(preparedMacroTxn.getPid(), new HashMap<>()));
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void p2pRevertV2InsufficientPrepaidAmount(){

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    MacroTransaction preparedMacroTxn = walletService.p2pPrepare(new CustomerP2PPrepareDTO(customerWallet.getUserId(), new Money(Currency.JPY, 300), null));
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(preparedMacroTxn.getPid(), customerWallet1.getUserId(), null));

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet1.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 200), null));
    walletService.capture(new CustomerCaptureDTO(Collections.singletonList(txnId1), null));

    walletService.p2pRevertV2(new RevertDTO(preparedMacroTxn.getPid(), new HashMap<>()));
  }

  @Test(expected = AccountTypeNotAllowedException.class)
  public void merchantReleaseSuccessCannotReleaseAgain() {
    long before = merchantWallet.emoney().balance().getCents();
    List<MerchantReleaseDTO> requests = release700YenSetup();
    Assert.assertEquals(700l, merchantWallet.emoney().balance().getCents().longValue());
    walletService.release(requests);
  }

  @Test
  public void merchantPayoutPrepareSuccess() {
    release700YenSetup();

    long beforeE = merchantWallet.emoney().balance().getCents();
    long beforePO = merchantWallet.payout().balance().getCents();

    Money payout = new Money(Currency.JPY, 600L);
    Money charge = new Money(Currency.JPY, 5);

    MerchantPayoutPrepareDTO prepareDTO = new MerchantPayoutPrepareDTO(String.valueOf(merchantWallet.getUserId()), payout, charge, null);
    walletService.payoutPrepare(prepareDTO);

    Assert.assertEquals(beforePO + payout.getCents(), merchantWallet.payout().balance().getCents().longValue());
    Assert.assertEquals(beforeE - payout.add(charge).getCents(), merchantWallet.emoney().balance().getCents().longValue());
  }

  @Test(expected = AccountTypeNotAllowedException.class)
  public void merchantPayoutConfirmSuccessCannotConfirmAgain() {
    release700YenSetup();
    Assert.assertEquals(700l, merchantWallet.emoney().balance().getCents().longValue());

    MerchantPayoutPrepareDTO prepareDTO = new MerchantPayoutPrepareDTO(String.valueOf(merchantWallet.getUserId()), new Money(Currency.JPY, 600L), new Money(Currency.JPY, 5), null);
    long txnId = walletService.payoutPrepare(prepareDTO);

    long beforeP = merchantWallet.payout().balance().getCents();
    walletService.payoutConfirm(new ConfirmOrCancelDTO(txnId, null));
    Assert.assertEquals(beforeP - 600, merchantWallet.payout().balance().getCents().longValue());

    walletService.payoutConfirm(new ConfirmOrCancelDTO(txnId, null));
  }

  @Test(expected = AlreadyRevertedException.class)
  public void merchantPayoutCancelSuccessCannotCancelAgain() {
    release700YenSetup();
    Assert.assertEquals(700l, merchantWallet.emoney().balance().getCents().longValue());

    long beforeP = merchantWallet.payout().balance().getCents();
    long beforeE = merchantWallet.emoney().balance().getCents();

    MerchantPayoutPrepareDTO prepareDTO = new MerchantPayoutPrepareDTO(String.valueOf(merchantWallet.getUserId()), new Money(Currency.JPY, 600L), new Money(Currency.JPY, 5), null);
    long txnId = walletService.payoutPrepare(prepareDTO);

    walletService.payoutCancel(new ConfirmOrCancelDTO(txnId, null));
    Assert.assertEquals(beforeP, merchantWallet.payout().balance().getCents().longValue());
    Assert.assertEquals(beforeE, merchantWallet.emoney().balance().getCents().longValue());

    walletService.payoutCancel(new ConfirmOrCancelDTO(txnId, null));
  }


  @Test
  public void createCampaignAccount(){
    CreateCampaignAccountDTO createCampaignAccountDTO = new CreateCampaignAccountDTO(campaignToken);
    walletService.createCampaignAccount(createCampaignAccountDTO);
    Account account = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    assertThat(account).isNotNull();
    assertThat(account.balance().getCents()).isEqualTo(0);
    assertThat(account.getType()).isEqualTo(AccountType.CAMPAIGN);
  }

  @Test
  public void updateSystemCampaignAccountToAllowNegativeBalance(){

    createCampaignAccount();

    Boolean budgetConstraintDisabled = null;
    Boolean negativeBalanceAllowed = true;
    UpdateCampaignAccountDTO updateCampaignAccountDTO = new UpdateCampaignAccountDTO(campaignToken, budgetConstraintDisabled, negativeBalanceAllowed, null);
    accountRepository.add(walletService.updateCampaignAccount(updateCampaignAccountDTO));
    Account campaign = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    assertThat(campaign.balance().getCents()).isEqualTo(0);
    assertThat(campaign.getMetadataByKey("_budget_constraint_disabled")).isNull();
    assertThat(campaign.getMetadataByKey("_negative_balance_allowed")).isEqualTo("true");
  }

  @Test
  public void balanceOfNegativeBalanceAllowedSystemCampaignCanBeMinus(){

    updateSystemCampaignAccountToAllowNegativeBalance();

    Money depositAmount = new Money(Currency.JPY, 300);
    CustomerCashbackV3DTO depositRequest = new CustomerCashbackV3DTO(campaignToken, customerWallet.getUserId(), depositAmount, CASHBACK.name(), metadata);

    // can be minus from 0
    long cashbackPid = walletService.cashbackV3(depositRequest).getPid();
    Account campaign = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    assertThat(campaign.balance().getCents()).isEqualTo(-300);
    Account cashback = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId()).cashback();
    assertThat(cashback.balance().getCents()).isEqualTo(300);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(cashbackPid);
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK, 300L, 300L, 0L, 300L)
        );

    // can be minus from minus
    cashbackPid = walletService.cashbackV3(depositRequest).getPid();
    campaign = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    assertThat(campaign.balance().getCents()).isEqualTo(-600);
    cashback = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId()).cashback();
    assertThat(cashback.balance().getCents()).isEqualTo(600);
    transactions = transactionRepository.transactionsOfPid(cashbackPid);
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK, 300L, 300L, 0L, 600L)
        );
  }

  @Test
  public void topupCampaignAccount(){
    createCampaignAccount();
    Money amount = new Money(Currency.JPY, 10000);
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, amount);
    walletService.topupCampaignAccount(topupCampaignAccountDTO);
    Account account = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    assertThat(account).isNotNull();
    assertThat(account.balance()).isEqualTo(amount);
    assertThat(account.getType()).isEqualTo(AccountType.CAMPAIGN);
  }

  @Test
  public void topupCampaignAccountFail(){
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, amount);
    Assertions.assertThatThrownBy(()->walletService.topupCampaignAccount(topupCampaignAccountDTO)).isInstanceOf(
        AccountNotFoundException.class);
  }

  @Test
  public void topupCampaignReverse(){
    createCampaignAccount();
    Money amount = new Money(Currency.JPY, 10000);
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, amount);
    long pid = walletService.topupCampaignAccount(topupCampaignAccountDTO);
    walletService.topupCampaignReverse(new RevertDTO(pid, null));

    Account account = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    assertThat(account).isNotNull();
    assertThat(account.balance()).isEqualTo(new Money(Currency.JPY, 0));
    assertThat(account.getType()).isEqualTo(AccountType.CAMPAIGN);
  }

  @Test(expected = AlreadyRevertedException.class)
  public void topupCampaignReverseAlreadyReversed(){
    createCampaignAccount();
    Money amount = new Money(Currency.JPY, 10000);
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, amount);
    long pid = walletService.topupCampaignAccount(topupCampaignAccountDTO);
    walletService.topupCampaignReverse(new RevertDTO(pid, null));
    walletService.topupCampaignReverse(new RevertDTO(pid, null));
  }

  @Test(expected = TransactionNotFoundException.class)
  public void topupCampaignReverseNotFoundTransaction(){
    createCampaignAccount();
    Money amount = new Money(Currency.JPY, 10000);
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, amount);
    long pid = walletService.topupCampaignAccount(topupCampaignAccountDTO);
    walletService.topupCampaignReverse(new RevertDTO(pid + 1, null));
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void topupCampaignReverseNotEnoughMoney(){
    createCampaignAccount();

    Money amount = new Money(Currency.JPY, 10000);
    TopupCampaignAccountDTO topupCampaignAccountDTO = new TopupCampaignAccountDTO(campaignToken, amount);
    long pid = walletService.topupCampaignAccount(topupCampaignAccountDTO);

    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    walletService.cashbackV3(customerCashbackDTO);


    walletService.topupCampaignReverse(new RevertDTO(pid, null));
  }

  @Test
  public void customerTopupFailedForMinimumLimit() {
    expectedException.expect(TransactionLimitException.class);
    expectedException.expectMessage("topup minimum: 100");

    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    Account source = externalWallet.source();

    Money amount = new Money(Currency.JPY, 99L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");

    walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, new HashMap<String, String>()));
  }

  @Test
  public void customerTopupFailedForPrepaidLimit() {
    expectedException.expect(BalanceLimitException.class);
    expectedException.expectMessage("exceeds 1,000,000 limit");


    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();

    Money amount = new Money(Currency.JPY, 400001L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "BANK");

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP + amount.getCents(), customerWallet.prepaid().balance().getCents().longValue());

    txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    meta.put("order_id", "1234");

    walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, meta));
  }

  @Test
  public void customerTopupFailedForMaximumLimit() {
    expectedException.expect(TransactionLimitException.class);
    expectedException.expectMessage("topup maximum: 500000");

    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    Account source = externalWallet.source();

    Money amount = new Money(Currency.JPY, 500001L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");

    walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, new HashMap<String, String>()));
  }

  @Test
  public void customerTopupPrepareThenCancel() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    Account source = externalWallet.source();

    Money amount = new Money(Currency.JPY, 300L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "BANK");

    Long beforeI = customerWallet.incoming().balance().getCents();
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, meta));
    Assert.assertEquals(beforeI + amount.getCents(), customerWallet.incoming().balance().getCents().longValue());

    walletService.customerTopupCancel(new ConfirmOrCancelDTO(txnId, null));
    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents());
  }

  @Test
  public void customerTopupPrepareThenConfirm() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();

    Money amount = new Money(Currency.JPY, 300L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "BANK");

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP + 300, customerWallet.prepaid().balance().getCents().longValue());
  }

  @Test
  public void customerTopupPrepareThenConfirmThenRevertWithNonKycUser() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();

    Money amount = new Money(Currency.JPY, 300L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "BANK");

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(customerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP + 300, customerWallet.prepaid().balance().getCents().longValue());

    walletService.customerTopupRevert(new RevertDTO(txnId, meta));
    Assert.assertEquals(beforeP, customerWallet.prepaid().balance().getCents().longValue());
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Test
  public void customerTopupPrepareThenConfirmThenRevertWithKycUser() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    Account source = externalWallet.source();

    Account emoney = kycCustomerWallet.emoney().get();
    long originalCustomerBalance = kycCustomerWallet.getTotalBalance();

    Money amount = new Money(Currency.JPY, 300L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "BANK");


    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(kycCustomerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));
    MacroTransaction macroTransaction = walletService.customerTopupRevert(new RevertDTO(txnId, meta));

    // verify wallet balance
    assertThat(originalCustomerBalance).isEqualTo(kycCustomerWallet.getTotalBalance());

    // verify transactions
    List<Transaction> transactions = macroTransaction.getTransactions();
    assertThat(transactions).extracting("srcUserId","srcAccountUuid","srcAmount","srcBalance","srcAccountType",
        "dstUserId","dstAccountUuid","dstAmount", "dstBalance", "dstAccountType").containsExactly(
        // revert(emoney -> source)
        tuple(kycCustomerWallet.getUserId(), emoney.getId(), amount.getCents(), originalCustomerBalance, EMONEY,
            externalWallet.getUserId(), source.getId(), amount.getCents(), 0L, SOURCE)
    );

    long derivedPid = transactions.get(0).getPid();
    // verify derived transaction
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(txnId);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type").containsExactly(
        tuple(txnId, derivedPid, DerivedTransactionType.REVERT)
    );


  }

  @Test
  public void kycCustomerTopupWithYJCard() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();

    Money amount = new Money(Currency.JPY, 300L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "CREDIT_CARD");

    long beforeI = kycCustomerWallet.incoming().balance().getCents();
    long beforeP = kycCustomerWallet.prepaid().balance().getCents();
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(kycCustomerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    Assert.assertEquals(beforeI, kycCustomerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP + 300, kycCustomerWallet.prepaid().balance().getCents().longValue());
  }

  @Test
  public void kycCustomerTopupWithBANK() {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();

    Money amount = new Money(Currency.JPY, 300L);
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");
    meta.put("payment_method_type", "YMONEY_BANK");

    long beforeI = kycCustomerWallet.incoming().balance().getCents();
    long beforeP = kycCustomerWallet.prepaid().balance().getCents();
    long beforeE = kycCustomerWallet.emoney().get().balance().getCents();
    long txnId = walletService.customerTopupPrepare(new CustomerTopupPrepare(String.valueOf(kycCustomerWallet.getUserId()), amount, meta));
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    Assert.assertEquals(beforeI, kycCustomerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP, kycCustomerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals(beforeE + 300, kycCustomerWallet.emoney().get().balance().getCents().longValue());
  }

  @Test
  public void customerGifcardTopupPrepareWithPrepaid() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction macroTransaction = walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "PREPAID", amount, new HashMap<>()));

    Assert.assertEquals(beforeI + amount.getCents(), customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP , customerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals("PREPAID", macroTransaction.getMetadata().get("accountType"));
    Assert.assertEquals(1, macroTransaction.getTransactions().size());
    Assert.assertEquals(AccountType.SOURCE, macroTransaction.getTransactions().get(0).getSrcAccountType());
    Assert.assertEquals(AccountType.INCOMING, macroTransaction.getTransactions().get(0).getDstAccountType());
  }

  @Test
  public void customerGifcardTopupPrepareWithCashback() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction macroTransaction = walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));

    Assert.assertEquals(beforeI + amount.getCents(), customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP, customerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals("CASHBACK", macroTransaction.getMetadata().get("accountType"));
    Assert.assertEquals(1, macroTransaction.getTransactions().size());
    Assert.assertEquals(AccountType.SOURCE, macroTransaction.getTransactions().get(0).getSrcAccountType());
    Assert.assertEquals(AccountType.INCOMING, macroTransaction.getTransactions().get(0).getDstAccountType());
  }

  @Test
  public void customerGiftcardTopupFailedForPrepaidLimit() {
    expectedException.expect(BalanceLimitException.class);
    expectedException.expectMessage("exceeds 1000000 prepaid account limit");

    Money amount = new Money(Currency.JPY, 1000001L);
    walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "PREPAID", amount, new HashMap<>()));
  }

  @Test
  public void customerGiftcardTopupPrepaidFailedForMinimumLimit() {
    expectedException.expect(TransactionLimitException.class);
    expectedException.expectMessage("giftcard prepaid minimum: 100");

    Money amount = new Money(Currency.JPY, 99);
    walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "PREPAID", amount, new HashMap<>()));
  }

  @Test
  public void customerGiftcardTopupCashbackSucceededWithSmallAmount() {
    Money amount = new Money(Currency.JPY, 99);
    try {
      walletService.customerGiftcardTopupPrepare(
              new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));
    } catch (TransactionLimitException e) {
      fail();
    }
  }

  @Test
  public void customerGiftcardTopupFailedWithCashbackPending() {
    expectedException.expect(AccountTypeNotAllowedException.class);
    expectedException.expectMessage("account type 'CASHBACK_PENDING' not allowed");

    Money amount = new Money(Currency.JPY, 1000001L);
    walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK_PENDING", amount, new HashMap<>()));
  }

  @Test
  public void customerGifcardTopupPrepareThenConfirmWithPrepaid() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction macroTransaction = walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "PREPAID", amount, new HashMap<>()));

    MacroTransaction result = walletService.customerGiftcardTopupConfirm(
            new ConfirmOrCancelDTO(macroTransaction.getPid(), new HashMap<>()));

    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeC, customerWallet.cashback().balance().getCents().longValue());
    Assert.assertEquals(beforeP + amount.getCents(), customerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals(2, result.getTransactions().size());
    Assert.assertEquals(AccountType.INCOMING, result.getTransactions().get(1).getSrcAccountType());
    Assert.assertEquals(AccountType.PREPAID, result.getTransactions().get(1).getDstAccountType());
  }


  @Test
  public void customerGifcardTopupPrepareThenConfirmWithCashback() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction macroTransaction = walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));

    MacroTransaction result = walletService.customerGiftcardTopupConfirm(
            new ConfirmOrCancelDTO(macroTransaction.getPid(), new HashMap<>()));

    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeC + amount.getCents(), customerWallet.cashback().balance().getCents().longValue());
    Assert.assertEquals(beforeP, customerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals(2, result.getTransactions().size());
    Assert.assertEquals(AccountType.INCOMING, result.getTransactions().get(1).getSrcAccountType());
    Assert.assertEquals(AccountType.CASHBACK, result.getTransactions().get(1).getDstAccountType());
  }

  @Test
  public void customerGifcardTopupPrepareThenCancel() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction macroTransaction = walletService.customerGiftcardTopupPrepare(
            new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));

    MacroTransaction result = walletService.customerGiftcardTopupCancel(
            new ConfirmOrCancelDTO(macroTransaction.getPid(), new HashMap<>()));

    Assert.assertEquals(macroTransaction.getPid(), result.getPid());
    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeC, customerWallet.cashback().balance().getCents().longValue());
    Assert.assertEquals(beforeP, customerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals(2, result.getTransactions().size());
    Assert.assertEquals(AccountType.INCOMING, result.getTransactions().get(1).getSrcAccountType());
    Assert.assertEquals(AccountType.SOURCE, result.getTransactions().get(1).getDstAccountType());
  }

  @Test
  public void customerGiftcardTopupRevertWithCashback() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction prepare = walletService.customerGiftcardTopupPrepare(
        new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));

    MacroTransaction confirm = walletService.customerGiftcardTopupConfirm(
        new ConfirmOrCancelDTO(prepare.getPid(), new HashMap<>()));

    Assert.assertEquals(beforeC + amount.getCents(), customerWallet.cashback().balance().getCents().longValue());

    MacroTransaction revert = walletService.customerGiftcardTopupRevert(new RevertDTO(confirm.getPid(), new HashMap<>()));
    Assert.assertEquals(beforeC , customerWallet.cashback().balance().getCents().longValue());

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(confirm.getPid());
    assertThat(derivedTransactions.size()).isEqualTo(1);
    assertThat(derivedTransactions.get(0).getDerivedPid()).isEqualTo(revert.getPid());
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);
  }

  @Test
  public void customerGiftcardTopupRevertWithPrepaid() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction prepare = walletService.customerGiftcardTopupPrepare(
        new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "PREPAID", amount, new HashMap<>()));

    MacroTransaction confirm = walletService.customerGiftcardTopupConfirm(
        new ConfirmOrCancelDTO(prepare.getPid(), new HashMap<>()));

    Assert.assertEquals(beforeP + amount.getCents(), customerWallet.prepaid().balance().getCents().longValue());

    MacroTransaction revert = walletService.customerGiftcardTopupRevert(new RevertDTO(confirm.getPid(), new HashMap<>()));
    Assert.assertEquals(beforeP , customerWallet.prepaid().balance().getCents().longValue());

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(confirm.getPid());
    assertThat(derivedTransactions.size()).isEqualTo(1);
    assertThat(derivedTransactions.get(0).getDerivedPid()).isEqualTo(revert.getPid());
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void customerGiftcardTopupRevertTwice() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction prepare = walletService.customerGiftcardTopupPrepare(
        new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));

    MacroTransaction confirm = walletService.customerGiftcardTopupConfirm(
        new ConfirmOrCancelDTO(prepare.getPid(), new HashMap<>()));

    Assert.assertEquals(beforeC + amount.getCents(), customerWallet.cashback().balance().getCents().longValue());

    MacroTransaction revert = walletService.customerGiftcardTopupRevert(new RevertDTO(confirm.getPid(), new HashMap<>()));
    Assert.assertEquals(beforeC , customerWallet.cashback().balance().getCents().longValue());

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(confirm.getPid());
    assertThat(derivedTransactions.size()).isEqualTo(1);
    assertThat(derivedTransactions.get(0).getDerivedPid()).isEqualTo(revert.getPid());
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);

    revert = walletService.customerGiftcardTopupRevert(new RevertDTO(confirm.getPid(), new HashMap<>()));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void customerGiftcardTopupPrepareThenRevert() {
    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction prepare = walletService.customerGiftcardTopupPrepare(
        new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "PREPAID", amount, new HashMap<>()));

    Assert.assertEquals(beforeI + amount.getCents(), customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeP , customerWallet.prepaid().balance().getCents().longValue());
    Assert.assertEquals("PREPAID", prepare.getMetadata().get("accountType"));

    MacroTransaction revert = walletService.customerGiftcardTopupRevert(new RevertDTO(prepare.getPid(), new HashMap<>()));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void customerGiftcardTopupCancelThenRevert() {

    Money amount = new Money(Currency.JPY, 300L);

    long beforeI = customerWallet.incoming().balance().getCents();
    long beforeC = customerWallet.cashback().balance().getCents();
    long beforeP = customerWallet.prepaid().balance().getCents();
    MacroTransaction prepare = walletService.customerGiftcardTopupPrepare(
        new CustomerGiftcardTopupPrepare(String.valueOf(customerWallet.getUserId()), "CASHBACK", amount, new HashMap<>()));

    MacroTransaction cancel = walletService.customerGiftcardTopupCancel(
        new ConfirmOrCancelDTO(prepare.getPid(), new HashMap<>()));

    Assert.assertEquals(prepare.getPid(), cancel.getPid());
    Assert.assertEquals(beforeI, customerWallet.incoming().balance().getCents().longValue());
    Assert.assertEquals(beforeC, customerWallet.cashback().balance().getCents().longValue());
    Assert.assertEquals(beforeP, customerWallet.prepaid().balance().getCents().longValue());

    MacroTransaction revert = walletService.customerGiftcardTopupRevert(new RevertDTO(prepare.getPid(), new HashMap<>()));
  }

  @Test
  public void customerCashback() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackDTO customerCashbackDTO = new CustomerCashbackDTO(campaignToken, nonKycCustomerId, amount, meta);
    long pid = walletService.cashback(customerCashbackDTO);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getCashback();
    assertThat(customerAccount.balance().getCents()).isEqualTo(1);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
  }

  @Test(expected = InvalidCashbackRequestException.class)
  public void customerCashbackV2WhenInvalidCashbackRequestException() {
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackV2DTO customerCashbackV2DTO = new CustomerCashbackV2DTO(campaignToken, nonKycCustomerId, Money.ZERO_JPY, Money.ZERO_JPY, meta);
    long pid = walletService.cashback(customerCashbackV2DTO);
  }

  @Test
  public void customerCashbackV2WithCashback() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackV2DTO customerCashbackV2DTO = new CustomerCashbackV2DTO(campaignToken, nonKycCustomerId, amount, Money.ZERO_JPY, meta);
    long pid = walletService.cashback(customerCashbackV2DTO);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getCashback();
    assertThat(customerAccount.balance().getCents()).isEqualTo(1);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
  }

  @Test
  public void customerCashbackV2WithPrepaid() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackV2DTO customerCashbackV2DTO = new CustomerCashbackV2DTO(campaignToken, nonKycCustomerId, Money.ZERO_JPY, amount, meta);
    long pid = walletService.cashback(customerCashbackV2DTO);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getPrepaid();
    assertThat(customerAccount.balance().getCents()).isEqualTo(1);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
  }

  /*
   * Regular cases for customer cashback V3
   */
  @Test
  public void customerCashbackV3WithCashbackShouldSucceed() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.CASHBACK.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getCashback();
    assertThat(customerAccount.balance().getCents()).isEqualTo(1);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
  }

  @Test
  public void customerCashbackV3WithPrepaidShouldSucceed() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.PREPAID.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getPrepaid();
    assertThat(customerAccount.balance().getCents()).isEqualTo(1);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
  }

  @Test
  public void customerCashbackV3WithCashbackPendingShouldSucceed() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    String activationDate = DateUtils.dateToString(DateUtils.dateFromDays("10"));
    meta.put("activation_date", activationDate);

    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.CASHBACK_PENDING.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getPrepaid();
    assertThat(customerAccount.balance().getCents()).isEqualTo(0);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
    assertThat(t.getMetadataByKey("activation_date")).isEqualTo(activationDate);
  }

  /*
   * system cashback to CASHBACK_EXPIRABLE tests
   *   - should succeed
   *   - should add coresponding type of metadata(SYSTEM_CASHBACK)
   */

  @Test
  public void customerCashbackV3WithCashbackExpirableShouldSucceed() {
    topupCampaignAccount();

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    String expireDate = DateUtils.dateToString(DateUtils.dateFromDays("10"));
    meta.put("expiry_date", expireDate);

    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.CASHBACK_EXPIRABLE.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC - amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getPrepaid();
    assertThat(customerAccount.balance().getCents()).isEqualTo(0);

    Transaction t = transactionRepository.transactionsOfPid(pid).get(0);
    assertThat(t.cashbackTransaction()).isTrue();
    assertThat(t.getSrcAmount()).isEqualTo(1);
    assertThat(t.getDstAmount()).isEqualTo(1);
    assertThat(t.getMetadataByKey("key")).isEqualTo("value");
    assertThat(t.getMetadataByKey("expiry_date")).isEqualTo(expireDate);
    assertThat(t.getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_CAMPAIGN.name());
    assertThat(t.getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
  }

  @Test
  public void customerCashbackV3WithCashbackExpirableShouldAddValidTypeToMetadata() {
    topupCampaignAccount();

    Map<String, String> meta = new HashMap<String, String>();
    String expireDate = DateUtils.dateToString(DateUtils.dateFromDays("10"));
    meta.put("expiry_date", expireDate);

    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.CASHBACK_EXPIRABLE.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isNotEqualTo(0);
    Transaction t = transactions.get(0);
    assertThat(t.getMetadataByKey("expiry_date")).isEqualTo(expireDate);
    assertThat(t.getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_CAMPAIGN.name());
    assertThat(t.getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
  }

  @Test
  public void customerCashbackV3WithCashbackPendingWithExpiryDateShouldAddValidTypeToMetadata() {
    topupCampaignAccount();

    Map<String, String> meta = new HashMap<String, String>();
    String activationDate = DateUtils.dateToString(DateUtils.dateFromDays("5"));
    meta.put("activation_date", activationDate);
    String expireDate = DateUtils.dateToString(DateUtils.dateFromDays("10"));
    meta.put("expiry_date", expireDate);

    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.CASHBACK_PENDING.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isNotEqualTo(0);
    Transaction t = transactions.get(0);
    assertThat(t.getMetadataByKey("expiry_date")).isEqualTo(expireDate);
    assertThat(t.getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_CAMPAIGN.name());
    assertThat(t.getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
  }

  @Test
  public void customerCashbackV3WithCashbackShouldNotAddTypeToMetadata() {
    topupCampaignAccount();

    Map<String, String> meta = new HashMap<String, String>();
    String expireDate = DateUtils.dateToString(DateUtils.dateFromDays("10"));
    meta.put("expiry_date", expireDate);

    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.CASHBACK.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isNotEqualTo(0);
    Transaction t = transactions.get(0);
    assertThat(t.getMetadata()).doesNotContainKeys(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE);
    assertThat(t.getMetadata()).doesNotContainKeys(MetaKeys.CAMPAIGN_TOKEN_SNAKE);
  }

  @Test
  public void customerCashbackV3WithPrepaidShouldNotAddTypeToMetadata() {
    topupCampaignAccount();

    Map<String, String> meta = new HashMap<String, String>();

    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, AccountType.PREPAID.name(), meta);
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isNotEqualTo(0);
    Transaction t = transactions.get(0);
    assertThat(t.getMetadata()).doesNotContainKeys(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE);
    assertThat(t.getMetadata()).doesNotContainKeys(MetaKeys.CAMPAIGN_TOKEN_SNAKE);
  }

  @Test(expected = InvalidCashbackRequestException.class)
  public void customerCashbackV3WithCashbackShouldFailWithInvalidAccountType() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(
        campaignToken, nonKycCustomerId, amount, "INVALID_ACCOUNT_TYPE", meta);
    walletService.cashbackV3(customerCashbackV3DTO);
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void customerCashbackFailedWhenCampaignAccountLackOfMoney() {
    createCampaignAccount();
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackDTO customerCashbackDTO = new CustomerCashbackDTO(campaignToken, nonKycCustomerId, amount, meta);
    long pid = walletService.cashback(customerCashbackDTO);
  }

  @Test
  public void fullCashbackReverseWithOrderId() {
    topupCampaignAccount();
    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);

    Map<String, String> meta = new HashMap<>();
    meta.put("order_id", "02929534108122202112");

    CustomerCashbackDTO customerCashbackDTO = new CustomerCashbackDTO(campaignToken, nonKycCustomerId, amount, meta);
    long pid = walletService.cashback(customerCashbackDTO);
    long balanceC = campaignAccount.balance().getCents();

    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, amount, new HashMap<>());
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getCashback();
    assertThat(customerAccount.balance().getCents()).isEqualTo(0);

    List<Transaction> old = transactionRepository.transactionsOfPid(pid);
    assertThat(old.size()).isEqualTo(1);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(1);

    assertThat(old.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(newTxn.get(0).getSrcAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(newTxn.get(0).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(newTxn.get(0).getMetadataByKey("order_id")).isEqualTo("02929534108122202112");
    assertThat(newTxn.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(pid));
  }

  @Test
  public void fullCashbackReverseWithoutOrderID() {
    topupCampaignAccount();
    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    CustomerCashbackDTO customerCashbackDTO = new CustomerCashbackDTO(campaignToken, nonKycCustomerId, amount, new HashMap<>());
    long pid = walletService.cashback(customerCashbackDTO);
    long balanceC = campaignAccount.balance().getCents();

    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, amount, new HashMap<>());
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + amount.getCents());

    Account customerAccount = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId).getCashback();
    assertThat(customerAccount.balance().getCents()).isEqualTo(0);

    List<Transaction> old = transactionRepository.transactionsOfPid(pid);
    assertThat(old.size()).isEqualTo(1);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(1);

    assertThat(old.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(newTxn.get(0).getSrcAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(newTxn.get(0).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(newTxn.get(0).getMetadataByKey("order_id")).isNullOrEmpty();
    assertThat(newTxn.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(pid));
  }

  @Test(expected = AlreadyRevertedException.class)
  public void fullCashbackReverseAlreadyReverted() {
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    CustomerCashbackDTO customerCashbackDTO = new CustomerCashbackDTO(campaignToken, nonKycCustomerId, amount, new HashMap<>());
    long pid = walletService.cashback(customerCashbackDTO);

    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, amount, new HashMap<>());
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(1);

    walletService.cashbackReverse(customerCashbackReverseDTO);

  }

  @Test(expected = AlreadyExpiredException.class)
  public void fullCashbackReverseAlreadyExpired() {

    // Setup
    funding(AccountType.CASHBACK_EXPIRABLE, 300);
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    CustomerCashbackV3DTO depositRequest = getSystemCashbackDepositRequest(campaignToken, AccountType.CASHBACK_EXPIRABLE, depositAmount, metadata);
    long depositPid = walletService.cashbackV3(depositRequest).getPid();

    // verify account balance after setup
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    SystemWallet swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(600L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(600L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9700);

    // expire/prepare
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>());
    List<TransactionV2DTO> preparePids = walletService.expireCashbackPrepare(prepare);

    // expire/confirm
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(0).getPid()), new HashMap<>());
    TransactionV2DTO confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(0).getPid()).isEqualTo(confirmPid.getPid());

    // verify account balance after expiry/confirm
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(300L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(300L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(10000);

    // reverse
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    walletService.cashbackReverse(reverseRequest);
  }

  @Test
  public void fullCashbackReverseUsePrepaid() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);

    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK", new HashMap<>());
    long cashbackPid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    long afterCashbackSystemCampaignBalance = campaignAccount.balance().getCents();

    walletService.authorize(new CustomerAuthorizeDTO(nonKycCustomerId, merchantId, new Money(Currency.JPY, 1250), null));
    assertThat(cashbackExpirable.balance().getCents()).isEqualTo(0);
    assertThat(cashback.balance().getCents()).isEqualTo(50);
    assertThat(prepaid.balance().getCents()).isEqualTo(800);

    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(cashbackPid, amount, new HashMap<>());
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(afterCashbackSystemCampaignBalance + amount.getCents());

    List<Transaction> old = transactionRepository.transactionsOfPid(cashbackPid);
    assertThat(old.size()).isEqualTo(1);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(2);

    assertThat(old.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK);

    assertThat(newTxn.get(0).getSrcAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(newTxn.get(0).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(newTxn.get(1).getSrcAccountType()).isEqualTo(AccountType.PREPAID);
    assertThat(newTxn.get(1).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(newTxn.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(cashbackPid));
    assertThat(newTxn.get(1).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(cashbackPid));

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(cashbackPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void fullCashbackReverseWithCashbackExpirable() {

    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    CustomerCashbackV3DTO depositRequest = getSystemCashbackDepositRequest(campaignToken, AccountType.CASHBACK_EXPIRABLE, depositAmount, metadata);
    long depositPid = walletService.cashbackV3(depositRequest).getPid();

    // verify account balance after setup
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    SystemWallet swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(300L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(300L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9700);

    // Main
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    BestEffortChargeDTO reverseResponse = walletService.cashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getCharged()).isEqualTo(depositAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(depositAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            Tuple.tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            Tuple.tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            Tuple.tuple(depositPid, depositAmount, customerWallet.getUserId(), CashbackExpiryStatus.REVERSED)
        );

    // verify account balance after setup
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(10000);
  }

  @Test(expected = AlreadyRevertedException.class)
  public void fullCashbackReverseUsePrepaidAlreadyReverted() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK", new HashMap<>());
    long cashbackPid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    walletService.authorize(new CustomerAuthorizeDTO(nonKycCustomerId, merchantId, new Money(Currency.JPY, 1250), null));
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(cashbackPid, amount, new HashMap<>());

    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(2);

    walletService.cashbackReverse(customerCashbackReverseDTO);
  }

  @Test(expected = AlreadyRevertedException.class)
  public void CashbackReverseUsePrepaidMultipleTimesReverted() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK", new HashMap<>());
    long cashbackPid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    walletService.authorize(new CustomerAuthorizeDTO(nonKycCustomerId, merchantId, new Money(Currency.JPY, 1250), null));
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(cashbackPid, new Money(Currency.JPY, 90), new HashMap<>());

    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(2);

    walletService.cashbackReverse(new CustomerCashbackReverseDTO(cashbackPid, new Money(Currency.JPY, 10), null));
  }

  @Test
  public void partialCashbackReverseUsePrepaid() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);

    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK", new HashMap<>());
    long cashbackPid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    long balanceC = campaignAccount.balance().getCents();

    walletService.authorize(new CustomerAuthorizeDTO(nonKycCustomerId, merchantId, new Money(Currency.JPY, 2050), null));
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(50);

    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(cashbackPid, amount, new HashMap<>());
    BestEffortChargeDTO reverseResult = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(reverseResult.getCharged().getCents()).isEqualTo(50);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + reverseResult.getCharged().getCents()); // only recovered 50

    List<Transaction> old = transactionRepository.transactionsOfPid(cashbackPid);
    assertThat(old.size()).isEqualTo(1);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(reverseResult.getPid());
    assertThat(newTxn.size()).isEqualTo(1);

    assertThat(old.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK);

    assertThat(newTxn.get(0).getSrcAccountType()).isEqualTo(AccountType.PREPAID);
    assertThat(newTxn.get(0).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(newTxn.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(cashbackPid));

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(cashbackPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void partialCashbackReverseWithCashbackExpirable() {

    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    CustomerCashbackV3DTO depositRequest = getSystemCashbackDepositRequest(campaignToken, AccountType.CASHBACK_EXPIRABLE, depositAmount, metadata);
    long depositPid = walletService.cashbackV3(depositRequest).getPid();

    // verify account balance after setup
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    SystemWallet swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(300L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(300L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9700);

    // Main
    Money partialReverseAmount = new Money(Currency.JPY, 200);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, partialReverseAmount, new HashMap<>());
    BestEffortChargeDTO reverseResponse = walletService.cashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getCharged()).isEqualTo(partialReverseAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(partialReverseAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            Tuple.tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.CAMPAIGN, partialReverseAmount.getCents(), partialReverseAmount.getCents(), 100L, 0L)
        );
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            Tuple.tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    List<CashbackExpiry> originalCashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(originalCashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            Tuple.tuple(depositPid, depositAmount, customerWallet.getUserId(), CashbackExpiryStatus.REVERSED)
        );
    List<CashbackExpiry> partialCashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(reverseResponse.getPid());
    assertThat(partialCashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            Tuple.tuple(reverseResponse.getPid(), depositAmount.minus(partialReverseAmount), customerWallet.getUserId(), CashbackExpiryStatus.COMPLETED)
        );
    assertThat(partialCashbackExpiry.get(0).getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_CAMPAIGN.name());
    assertThat(partialCashbackExpiry.get(0).getMetadata().get(MetaKeys.ORDER_ID)).isEqualTo(orderId);

    // verify account balances after partial reverse
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(100L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(100L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9900);
  }

  @Test(expected = AlreadyRevertedException.class)
  public void partialCashbackReverseUsePrepaidAlreadyReverted() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK", new HashMap<>());
    long pid = walletService.cashbackV3(customerCashbackV3DTO).getPid();

    walletService.authorize(new CustomerAuthorizeDTO(nonKycCustomerId, merchantId, new Money(Currency.JPY, 2050), null));
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, amount, new HashMap<>());
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(1);

    walletService.cashbackReverse(customerCashbackReverseDTO);
  }

  @Test
  public void zeroCashbackReverse() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);

    Money cashbackAmount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    CustomerCashbackV3DTO customerCashbackV3DTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, cashbackAmount, "CASHBACK", new HashMap<>());
    long cashbackPid = walletService.cashbackV3(customerCashbackV3DTO).getPid();
    long balanceC = campaignAccount.balance().getCents();

    walletService.authorize(new CustomerAuthorizeDTO(nonKycCustomerId, merchantId, new Money(Currency.JPY, 2100), null)); // spent all
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0);

    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(cashbackPid, cashbackAmount,  new HashMap<>());
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(result.getCharged().getCents()).isEqualTo(0);
    assertThat(result.getPid()).isNull();
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC); // Nothing recovered.
  }

  @Test
  public void fullCashbackReverseWithPending() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money cashbackAmount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
      put("attributed_campaign_id", "123");
      put("registration_id", "1234");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, cashbackAmount, "CASHBACK_PENDING", depositMetadate);
    long cashbackPid = walletService.cashbackV3(customerCashbackDTO).getPid();
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> reverseMetadate = new HashMap<String, String>(){{
      put("code", "clm_fraud");
    }};
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(cashbackPid, cashbackAmount, reverseMetadate);
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(result.getCharged().getCents()).isEqualTo(100L);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + cashbackAmount.getCents());

    List<Transaction> txns = transactionRepository.transactionsOfPid(cashbackPid);
    assertThat(txns.size()).isEqualTo(2);

    assertThat(txns.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(txns.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(txns.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(txns.get(1).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(txns.get(1).getMetadataByKey("attributed_campaign_id")).isEqualTo("123");
    assertThat(txns.get(1).getMetadataByKey("registration_id")).isEqualTo("1234");
  }

  @Test
  public void pertialCashbackReverseWithPending() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
      put("attributed_campaign_id", "123");
      put("registration_id", "1234");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    long pid = walletService.cashbackV3(customerCashbackDTO).getPid();
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> reverseMetadate = new HashMap<String, String>(){{
      put("code", "clm_fraud");
    }};
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, new Money("JPY", 50), reverseMetadate);
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(result.getCharged().getCents()).isEqualTo(50L);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + 50L);

    List<Transaction> old = transactionRepository.transactionsOfPid(pid);
    assertThat(old.size()).isEqualTo(2);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(result.getPid());
    assertThat(newTxn.size()).isEqualTo(1);

    assertThat(old.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(old.get(0).getDstAmount()).isEqualTo(100L);
    assertThat(old.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(old.get(1).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getSrcAmount()).isEqualTo(100L);
    assertThat(old.get(1).getMetadataByKey("attributed_campaign_id")).isEqualTo("123");
    assertThat(old.get(1).getMetadataByKey("registration_id")).isEqualTo("1234");

    assertThat(newTxn.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(newTxn.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(newTxn.get(0).getDstAmount()).isEqualTo(50L);
  }

  @Test
  public void cashbackSingleRelease() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    long pid = walletService.cashbackV3(customerCashbackDTO).getPid();
    assertThat(customerWallet.cashback().balance().equals(Money.ZERO_JPY));
    assertThat(customerWallet.cashbackPending().balance().equals(amount));

    Map<String, String> releaseMeta = new HashMap<String, String>(){{ put("key", "value"); }};
    long rpid = walletService.cashbackReleaseV3(new ConfirmOrCancelDTO(pid, releaseMeta));
    assertThat(rpid).isEqualTo(pid);
    assertThat(customerWallet.cashback().balance().equals(amount));
    assertThat(customerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(0).getMetadataByKey("activation_date")).isEqualTo("2019-01-01T19:00:01");
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
  }

  @Test
  public void cashbackSingleReleaseToExpirable() {
    topupCampaignAccount();

    String activationDate = DateUtils.dateToString(DateUtils.dateFromDays("3"));
    String expiryDate = DateUtils.dateToString(DateUtils.dateFromDays("5"));

    Money amount = new Money(Currency.JPY, 100);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", activationDate);
      put("expiry_date", expiryDate);
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    long pid = walletService.cashbackV3(customerCashbackDTO).getPid();
    assertThat(customerWallet.cashbackPending().balance().equals(amount));

    Map<String, String> releaseMeta = new HashMap<String, String>(){{ put("key", "value"); }};
    long rpid = walletService.cashbackReleaseV3(new ConfirmOrCancelDTO(pid, releaseMeta));
    assertThat(rpid).isEqualTo(pid);
    assertThat(customerWallet.cashback().balance().equals(amount));
    assertThat(customerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(0).getMetadataByKey("activation_date")).isEqualTo(activationDate);
    assertThat(transactions.get(0).getMetadataByKey("expiry_date")).isEqualTo(expiryDate);
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_CAMPAIGN.name());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
  }


  @Test
  public void merchantCashbackSingleReleaseToExpirable() {
    // funding nonkyc customer wallet
    long originalBalance = 200;
    funding(AccountType.CASHBACK_EXPIRABLE, originalBalance);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money cashback = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date activationDate = DateUtils.dateFromDays("4");
    metadata.put("activation_date", DateUtils.dateToString(activationDate));
    Date expiryDate = DateUtils.dateFromDays("6");
    // adding expiry_date means money is to be moved from cashback_pending to cashback_expirable
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, cashback, metadata);
    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(depositRequest);
    final long pid = Long.valueOf(response.getPid());
    assertThat(customerWallet.cashbackExpirable().balance().equals(originalBalance));
    assertThat(customerWallet.cashbackPending().balance().getCents()).isEqualTo(cashback.getCents());

    List<CashbackExpiry> tcashbackExpiries = cashbackExpiryRepository.cashbackExpiryOfPid(pid);
    Assertions.assertThat(tcashbackExpiries.size()).isEqualTo(0);

    // release to cashback_expirable account
    Map<String, String> releaseMeta = new HashMap<String, String>();
    releaseMeta.put("key", "value");
    ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(pid, releaseMeta);
    long rpid = walletService.cashbackReleaseV3(request);
    assertThat(rpid).isEqualTo(pid);

    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(cashback.getCents() + originalBalance);// 500

    assertThat(customerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(transactions.get(1).getSrcBalance()).isEqualTo(customerWallet.getTotalBalance());
    assertThat(transactions.get(1).getSrcBalance()).isEqualTo(transactions.get(1).getDstBalance());
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
    assertThat(transactions.get(1).getMetadataByKey(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(transactions.get(1).getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    assertThat(transactions.get(1).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());

    List<CashbackExpiry> cashbackExpiries = cashbackExpiryRepository.cashbackExpiryOfPid(pid);
    Assertions.assertThat(cashbackExpiries.size()).isEqualTo(1);
    CashbackExpiry record = cashbackExpiries.get(0);

    Assertions.assertThat(record.getStatus()).isEqualTo(CashbackExpiryStatus.COMPLETED);
    Assertions.assertThat(record.getCashback().getCents()).isEqualToComparingFieldByField(cashback.getCents());
    Assertions.assertThat(record.getExpiryDate()).isEqualTo(expiryDate);
    Assertions.assertThat(record.getUserId()).isEqualTo(customerWallet.getUserId());
    Assertions.assertThat(record.getMetadata().get(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    Assertions.assertThat(record.getMetadata().get(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    Assertions.assertThat(record.getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());
  }

  @Test
  public void cashbackBatchRelease() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount1 = new Money(Currency.JPY, 100);
    Money amount2 = new Money(Currency.JPY, 200);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    long pid1 = walletService.cashbackV3( new CustomerCashbackV3DTO(campaignToken, customerWallet.getUserId(), amount1, "CASHBACK_PENDING", depositMetadate) ).getPid();
    long pid2 = walletService.cashbackV3( new CustomerCashbackV3DTO(campaignToken, kycCustomerWallet.getUserId(), amount2, "CASHBACK_PENDING", depositMetadate) ).getPid();

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money amount3 = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date activationDate = DateUtils.dateFromDays("4");
    metadata.put("activation_date", DateUtils.dateToString(activationDate));
    Date expiryDate = DateUtils.dateFromDays("6");
    // adding expiry_date means money is to be moved from cashback_pending to cashback_expirable
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, amount3, metadata);
    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(depositRequest);
    final long pid3 = Long.valueOf(response.getPid());

    assertThat(customerWallet.cashbackPending().balance().getCents()).isEqualTo(amount1.getCents() + amount3.getCents());
    assertThat(kycCustomerWallet.cashbackPending().balance().getCents()).isEqualTo(amount2.getCents());

    Map<String, String> releaseMeta = new HashMap<String, String>(){{ put("key", "value"); }};
    List<BatchRequestElement<ConfirmOrCancelDTO>> requests = Arrays.asList(
        new BatchRequestElement<>(String.valueOf(pid1), new ConfirmOrCancelDTO(pid1, releaseMeta)),
        new BatchRequestElement<>(String.valueOf(pid2), new ConfirmOrCancelDTO(pid2, releaseMeta)),
        new BatchRequestElement<>(String.valueOf(pid3), new ConfirmOrCancelDTO(pid3, releaseMeta))
    );
    walletService.cashbackBatchReleaseV3(requests);
    assertThat(customerWallet.cashback().balance().getCents()).isEqualTo(amount1.getCents() + 1000); // 1100
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(amount3.getCents() + 200); // 500
    assertThat(customerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));

    assertThat(kycCustomerWallet.cashback().balance().getCents()).isEqualTo(amount2.getCents()); // 200
    assertThat(kycCustomerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid1);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount1.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");

    transactions = transactionRepository.transactionsOfPid(pid2);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount2.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");

    transactions = transactionRepository.transactionsOfPid(pid3);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount3.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");

    List<BatchControl> batchControls = batchControlRepository.findAll();
    assertThat(batchControls.size()).isEqualTo(3);
    assertThat(batchControls.get(0).getBatchId()).isNotNull();
    assertThat(batchControls.get(0).getRequestKey()).isEqualTo(String.valueOf(pid1));
    assertThat(batchControls.get(1).getRequestKey()).isEqualTo(String.valueOf(pid2));
    assertThat(batchControls.get(2).getRequestKey()).isEqualTo(String.valueOf(pid3));
  }

  @Test
  public void cashbackBatchReleaseIdempotently() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount1 = new Money(Currency.JPY, 100);
    Money amount2 = new Money(Currency.JPY, 200);
    Map<String, String> depositMetadate = new HashMap<String, String>() {{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    long pid1 = walletService.cashbackV3(
        new CustomerCashbackV3DTO(campaignToken, customerWallet.getUserId(), amount1,
            "CASHBACK_PENDING", depositMetadate)).getPid();
    long pid2 = walletService.cashbackV3(
        new CustomerCashbackV3DTO(campaignToken, kycCustomerWallet.getUserId(), amount2,
            "CASHBACK_PENDING", depositMetadate)).getPid();

    Map<String, String> releaseMeta = new HashMap<String, String>() {{
      put("key", "value");
    }};
    List<BatchRequestElement<ConfirmOrCancelDTO>> requests1 = Arrays.asList(
        new BatchRequestElement<>(String.valueOf(pid1), new ConfirmOrCancelDTO(pid1, releaseMeta))
    );
    walletService.cashbackBatchReleaseV3(requests1);
    assertThat(walletService.cashbackBatchReleaseV3(requests1)).isEmpty();
    assertThat(customerWallet.cashback().balance().equals(amount1));
    assertThat(customerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid1);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount1.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
    assertThat(batchControlRepository.count()).isEqualTo(1);
    assertThat(batchControlRepository.findAll().get(0).getRequestKey()).isEqualTo(String.valueOf(pid1));

    List<BatchRequestElement<ConfirmOrCancelDTO>> requests2 = Arrays.asList(
        new BatchRequestElement<>(String.valueOf(pid1), new ConfirmOrCancelDTO(pid1, releaseMeta)),
        new BatchRequestElement<>(String.valueOf(pid2), new ConfirmOrCancelDTO(pid2, releaseMeta))
    );
    List<BatchResponseElement<TransactionDTO>> response = walletService.cashbackBatchReleaseV3(requests2);
    assertThat(response.size()).isEqualTo(1);
    assertThat(response.get(0).getRequestKey()).isEqualTo(String.valueOf(pid2));
    assertThat(response.get(0).getResult().getPid()).isEqualTo(String.valueOf(pid2));
    transactions = transactionRepository.transactionsOfPid(pid2);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount2.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
    assertThat(batchControlRepository.count()).isEqualTo(2);
    assertThat(batchControlRepository.findAll().get(0).getRequestKey()).isEqualTo(String.valueOf(pid1));
    assertThat(batchControlRepository.findAll().get(1).getRequestKey()).isEqualTo(String.valueOf(pid2));

    List<BatchRequestElement<ConfirmOrCancelDTO>> requests3 = Arrays.asList(
        new BatchRequestElement<>(String.valueOf(pid2), new ConfirmOrCancelDTO(pid2, releaseMeta))
    );
    response = walletService.cashbackBatchReleaseV3(requests3);
    assertThat(response).isEmpty();
    assertThat(customerWallet.cashback().balance().equals(amount1));
    assertThat(customerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));
    assertThat(kycCustomerWallet.cashback().balance().equals(amount2));
    assertThat(kycCustomerWallet.cashbackPending().balance().equals(Money.ZERO_JPY));
    transactions = transactionRepository.transactionsOfPid(pid1);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount1.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
    transactions = transactionRepository.transactionsOfPid(pid2);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getDstAmount()).isEqualTo(amount2.getCents());
    assertThat(transactions.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(transactions.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(transactions.get(1).getMetadataByKey("key")).isEqualTo("value");
    List<BatchControl> batchControls = batchControlRepository.findAll();
    assertThat(batchControls.size()).isEqualTo(2);
    assertThat(batchControls.get(0).getBatchId()).isNotNull();
    assertThat(batchControls.get(0).getRequestKey()).isEqualTo(String.valueOf(pid1));
    assertThat(batchControls.get(1).getRequestKey()).isEqualTo(String.valueOf(pid2));
    assertThat(batchControlRepository.count()).isEqualTo(2);
  }


  @Test
  public void cashbackReleaseAfterRevert() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2018-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    long pid = walletService.cashbackV3(customerCashbackDTO).getPid();
    long balanceC = campaignAccount.balance().getCents();
    CustomerWallet cm = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    assertThat(cm.cashback().balance().getCents()).isEqualTo(1000);
    assertThat(cm.cashbackPending().balance().getCents()).isEqualTo(100);

    Map<String, String> reverseMetadate = new HashMap<String, String>(){{
      put("code", "clm_fraud");
    }};
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, amount, reverseMetadate);
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    assertThat(result.getCharged().getCents()).isEqualTo(100L);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + amount.getCents());
    cm = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    assertThat(cm.cashback().balance().getCents()).isEqualTo(1000);
    assertThat(cm.cashbackPending().balance().getCents()).isEqualTo(0);

    List<Transaction> old = transactionRepository.transactionsOfPid(pid);
    assertThat(old.size()).isEqualTo(2);

    assertThat(old.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(old.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(old.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(old.get(1).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);

    List<BatchRequestElement<ConfirmOrCancelDTO>> requests = Arrays.asList(
        new BatchRequestElement<>(String.valueOf(pid), new ConfirmOrCancelDTO(pid, null))
    );
    assertThat(walletService.cashbackBatchReleaseV3(requests)).isEmpty();
    cm = walletFactory.rebuildCustomerWalletByOwnerId(nonKycCustomerId);
    assertThat(cm.cashback().balance().getCents()).isEqualTo(1000);
    assertThat(cm.cashbackPending().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(batchControlRepository.count()).isEqualTo(0);
  }


  @Test(expected = InvalidCashbackRequestException.class)
  public void cashbackReverseWithPendingWhenInvalidCashbackRequestException() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    long pid = walletService.cashbackV3(customerCashbackDTO).getPid();

    Map<String, String> reverseMetadate = new HashMap<String, String>(){{
      put("code", "clm_fraud");
    }};
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(pid, new Money("JPY", 101), reverseMetadate);
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
  }

  @Test
  public void cashbackReverseAfterReleaseWithPendingCashback() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);
    topupCampaignAccount();

    Money amount = new Money(Currency.JPY, 100);
    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    Map<String, String> depositMetadate = new HashMap<String, String>(){{
      put("activation_date", "2019-01-01T19:00:01");
      put("order_id", "02929534108122202112");
    }};
    CustomerCashbackV3DTO customerCashbackDTO = new CustomerCashbackV3DTO(campaignToken, nonKycCustomerId, amount, "CASHBACK_PENDING", depositMetadate);
    long cashbackPid = walletService.cashbackV3(customerCashbackDTO).getPid();
    long balanceC = campaignAccount.balance().getCents();

    Map<String, String> releaseMeta = new HashMap<String, String>(){{ put("key", "value"); }};
    ConfirmOrCancelDTO cashbackReleaseDTO = new ConfirmOrCancelDTO(cashbackPid, releaseMeta);
    long relasedPid = walletService.cashbackReleaseV3(cashbackReleaseDTO);

    Map<String, String> reverseMetadata = new HashMap<String, String>(){{
      put("code", "clm_fraud");
    }};
    CustomerCashbackReverseDTO customerCashbackReverseDTO = new CustomerCashbackReverseDTO(relasedPid, amount, reverseMetadata);
    BestEffortChargeDTO result = walletService.cashbackReverse(customerCashbackReverseDTO);
    long reversedPid = result.getPid();
    assertThat(reversedPid).isNotEqualTo(cashbackPid);
    assertThat(result.getCharged().getCents()).isEqualTo(100L);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + amount.getCents());

    List<Transaction> released = transactionRepository.transactionsOfPid(cashbackPid);
    assertThat(released.size()).isEqualTo(2);
    assertThat(released.get(0).getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(released.get(0).getDstAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(released.get(1).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(released.get(1).getDstAccountType()).isEqualTo(AccountType.CASHBACK);

    List<Transaction> reversed = transactionRepository.transactionsOfPid(reversedPid);
    assertThat(reversed.size()).isEqualTo(1);
    assertThat(reversed.get(0).getSrcAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(reversed.get(0).getDstAccountType()).isEqualTo(AccountType.CAMPAIGN);
  }

  @Test
  public void merchantBatchReleaseSucess() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    Money tax = new Money(Currency.JPY, 50L);
    Money commision = new Money(Currency.JPY, 100L);
    Money relase = new Money(Currency.JPY, 400);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    SystemWallet systemWallet = walletFactory.rebuildSystemWallet();

    long beforeP = merchantWallet.payable().balance().getCents();
    long beforeE = merchantWallet.emoney().balance().getCents();
    long beforeC = systemWallet.getMerchantCommision().balance().getCents();
    long beforeT = systemWallet.getTax().balance().getCents();

    MerchantBatchReleaseDTO request = new MerchantBatchReleaseDTO(merchantWallet.getUserId(), relase, commision, tax, null);
    walletService.batchRelease(request);
    Assert.assertEquals(beforeP - relase.add(tax).add(commision).getCents(), merchantWallet.payable().balance().getCents().longValue());
    Assert.assertEquals(beforeE + relase.getCents(), merchantWallet.emoney().balance().getCents().longValue());
    Assert.assertEquals(beforeC + commision.getCents(), systemWallet.getMerchantCommision().balance().getCents().longValue());
    Assert.assertEquals(beforeT + tax.getCents(), systemWallet.getTax().balance().getCents().longValue());
  }

  @Test
  public void merchantBatchReleaseWithNegativeMPATest() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();
    long beforeE = merchantWallet.emoney().balance().getCents();
    // first release with positive payable balance 700
    MerchantBatchReleaseDTO request = new MerchantBatchReleaseDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 701), Money.ZERO_JPY, Money.ZERO_JPY, null);
    walletService.batchRelease(request);
    Assert.assertEquals(merchantWallet.payable().balance().getCents().longValue(), -1);
    Assert.assertEquals(merchantWallet.emoney().balance().getCents().longValue(), 701);
    // second release with negative payable balance -1
    request = new MerchantBatchReleaseDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 999), Money.ZERO_JPY, Money.ZERO_JPY, null);
    walletService.batchRelease(request);
    Assert.assertEquals(merchantWallet.payable().balance().getCents().longValue(), -1000);
    Assert.assertEquals(merchantWallet.emoney().balance().getCents().longValue(), 1700);
  }

  @Test (expected = NotEnoughMoneyException.class)
  public void merchantBatchReleaseViolatingNegativeMPALimitTest() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();
    long beforeE = merchantWallet.emoney().balance().getCents();
    // first release with positive payable balance 700
    MerchantBatchReleaseDTO request = new MerchantBatchReleaseDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 701), Money.ZERO_JPY, Money.ZERO_JPY, null);
    walletService.batchRelease(request);
    Assert.assertEquals(merchantWallet.payable().balance().getCents().longValue(), -1);
    Assert.assertEquals(merchantWallet.emoney().balance().getCents().longValue(), 701);
    // second release with negative payable balance -1
    request = new MerchantBatchReleaseDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 100000000000L), Money.ZERO_JPY, Money.ZERO_JPY, null);
    walletService.batchRelease(request);
    Assert.assertEquals(merchantWallet.payable().balance().getCents().longValue(), -1);
    Assert.assertEquals(merchantWallet.emoney().balance().getCents().longValue(), 701);
  }


  @Test
  public void merchantRefundSuccessWalletWithNonKycUser() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();

    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 1100), Money.ZERO_JPY, txnId1, null);
    walletService.refund(request);

    Assert.assertEquals(beforeP - 1100, merchantWallet.payable().balance().getCents().longValue());
  }

  @Test
  public void merchantRefundCashback() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));


    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));
    topupCampaignAccount();
    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    CustomerCashbackDTO customerCashbackDTO = new CustomerCashbackDTO(campaignToken, nonKycCustomerId, amount, meta);
    long pid = walletService.cashback(customerCashbackDTO);

    Account campaignAccount = walletService.findCampaignAccount(campaignToken);
    long balanceC = campaignAccount.balance().getCents();
    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 100), Money.ZERO_JPY, pid, null);
    long refundPid = walletService.refund(request);
    assertThat(campaignAccount.balance().getCents()).isEqualTo(balanceC + 100);

    final List<DerivedTransaction> derivedTransactions = derivedTransactionRepository
        .transactionsOfOriginalPid(pid);
    Assert.assertEquals(1, derivedTransactions.size());

    List<CashbackExpiry> cashbackExpiryList = cashbackExpiryRepository.cashbackExpiryOfPid(refundPid);
    assertThat(cashbackExpiryList.size()).isEqualTo(0);
  }

  @Test
  public void merchantRefundDstIncludeCashbackExpirable() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();
    Assert.assertEquals(200L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());

    Money authorizeAmount = new Money(Currency.JPY, 1500L);
    long authorizePid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), authorizeAmount, null));

    walletService.capture(new CustomerCaptureDTO(Arrays.asList(authorizePid), null));

    Money refundAmount = new Money(Currency.JPY, 1400);
    Map<String, String> refundMeta = new HashMap<>();
    refundMeta.put(MetaKeys.ORDER_ID, orderId);
    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), refundAmount, Money.ZERO_JPY, authorizePid, refundMeta);
    long refundPid = walletService.refund(request);

    Assert.assertEquals(100L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(1000L, cashback.balance().getCents().longValue());
    Assert.assertEquals(800L, prepaid.balance().getCents().longValue());
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(authorizePid);
    Assert.assertEquals(1, derivedTransactions.size());

    List<Transaction> transactions = transactionRepository.transactionsOfPid(refundPid);
    assertThat(transactions.size()).isEqualTo(3);
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(AccountType.PAYABLE, AccountType.PREPAID, 300L, 300L),
            tuple(AccountType.PAYABLE, AccountType.CASHBACK, 1000L, 1000L),
            tuple(AccountType.PAYABLE, AccountType.CASHBACK_EXPIRABLE, 100L, 100L)
        );
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(1).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isNull();
    assertThat(transactions.get(2).getMetadataByKey(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_EXPIRED.name());

    CashbackExpiry cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(refundPid).get(0);
    assertThat(cashbackExpiry.getPid()).isEqualTo(refundPid);
    assertThat(cashbackExpiry.getCashback().getCents()).isEqualTo(100L);
    long dayDiff = (cashbackExpiry.getExpiryDate().getTime() - cashbackExpiry.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24);
    assertThat(dayDiff).isEqualTo(59L); // It should be as close as possible to 60L. (e.g. 59.99..)
    assertThat(cashbackExpiry.getStatus()).isEqualTo(CashbackExpiryStatus.COMPLETED);
    assertThat(cashbackExpiry.getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.SYSTEM_EXPIRED.name());
    assertThat(cashbackExpiry.getMetadata().get(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(DateUtils.dateToString(cashbackExpiry.getExpiryDate())).isEqualTo(transactions.get(2).getMetadataByKey("expiry_date"));
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Test
  public void merchantPartialRefundWithKycUser() {
    // set up account
    funding(kycCustomerWallet, AccountType.CASHBACK_EXPIRABLE, 200);
    funding(kycCustomerWallet, AccountType.CASHBACK, 1000);
    funding(kycCustomerWallet, AccountType.PREPAID, 800);
    funding(kycCustomerWallet, AccountType.EMONEY, 500);

    Account cashbackExpirable = kycCustomerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = kycCustomerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = kycCustomerWallet.findAccountByType(AccountType.PREPAID).get();
    Account emoney = kycCustomerWallet.findAccountByType(EMONEY).get();
    Account payable = merchantWallet.findAccountByType(PAYABLE).get();

    long beforePaymentBalance = kycCustomerWallet.getTotalBalance();
    long paymentAmount = 2450L;

    // payment
    long pid = walletService.authorize(new CustomerAuthorizeDTO(kycCustomerWallet.getUserId(), merchantWallet.getUserId(), new Money(paymentAmount), metadata));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(pid), null));

    long merchantBalance = merchantWallet.getTotalBalance();

    // verify balance
    long restAmount = kycCustomerWallet.getTotalBalance();
    assertThat(restAmount).isEqualTo(50L);
    assertThat(cashbackExpirable.balance().getCents()).isEqualTo(0L);
    assertThat(cashback.balance().getCents()).isEqualTo(0L);
    assertThat(prepaid.balance().getCents()).isEqualTo(0L);
    assertThat(emoney.balance().getCents()).isEqualTo(50L);

    // refund
    long refundAmount = 2400L;
    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(refundAmount), Money.ZERO_JPY, pid, new HashMap<>());
    long refundPid = walletService.refund(request);

    // verify balance
    assertThat(kycCustomerWallet.getTotalBalance()).isEqualTo(beforePaymentBalance - paymentAmount + refundAmount);
    assertThat(cashbackExpirable.balance().getCents()).isEqualTo(150L);
    assertThat(cashback.balance().getCents()).isEqualTo(1000L);
    assertThat(prepaid.balance().getCents()).isEqualTo(800L);
    assertThat(emoney.balance().getCents()).isEqualTo(500L);

    // verify transaction record
    List<Transaction> transactions = transactionRepository.transactionsOfPid(refundPid);
    assertThat(transactions).extracting("srcUserId", "srcAccountUuid", "srcAmount", "srcBalance", "srcAccountType",
        "dstUserId", "dstAccountUuid", "dstAmount", "dstBalance", "dstAccountType").containsExactly(
        // refund
        // payment -> emoney
        tuple(merchantWallet.getUserId(), payable.getId(), 450L, merchantBalance - 450L, PAYABLE,
            kycCustomerWallet.getUserId(), emoney.getId(), 450L, restAmount + 450L, EMONEY),
        // payment -> prepaid
        tuple(merchantWallet.getUserId(), payable.getId(), 800L, (merchantBalance - 450L) - 800L, PAYABLE,
            kycCustomerWallet.getUserId(), prepaid.getId(), 800L, (restAmount + 450L) + 800L, PREPAID),
        // payment -> cashback
        tuple(merchantWallet.getUserId(), payable.getId(), 1000L, ((merchantBalance - 450L) - 800L) - 1000L, PAYABLE,
            kycCustomerWallet.getUserId(), cashback.getId(), 1000L, ((restAmount + 450L) + 800L) + 1000L, CASHBACK),
        // payment -> cashback_expirable
        tuple(merchantWallet.getUserId(), payable.getId(), 150L, (((merchantBalance - 450L) - 800L) - 1000L) - 150L, PAYABLE,
            kycCustomerWallet.getUserId(), cashbackExpirable.getId(), 150L, (((restAmount + 450L) + 800L) + 1000L) + 150L, CASHBACK_EXPIRABLE)
    );

    // verify derived transaction record
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(pid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type").containsExactly(
        tuple(pid, refundPid, DerivedTransactionType.REFUND)
    );
  }


  // TODO: This is not expected behavior, so should be fixed to refund to original account or not to allow multiple refund soon.
  @Test
  public void merchantPartialRefundMultipleTimes() {

    funding(AccountType.CASHBACK_EXPIRABLE, 100);
    funding(AccountType.CASHBACK, 100);
    funding(AccountType.PREPAID, 100);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();

    Money authorizeAmount = new Money(Currency.JPY, 300L);
    long authorizePid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), authorizeAmount, null));

    walletService.capture(new CustomerCaptureDTO(Arrays.asList(authorizePid), null));

    Money partialRefundAmount = new Money(Currency.JPY, 100);
    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), partialRefundAmount, Money.ZERO_JPY, authorizePid, null);

    long refundPid1 = walletService.refund(request);
    long refundPid2 = walletService.refund(request);
    long refundPid3 = walletService.refund(request);

    Assert.assertEquals(0L, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0L, cashback.balance().getCents().longValue());
    Assert.assertEquals(300L, prepaid.balance().getCents().longValue());

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(authorizePid);
    Assert.assertEquals(3, derivedTransactions.size());

    List<CashbackExpiry> emptyCashbackExpiries = cashbackExpiryRepository.cashbackExpiryOfPid(refundPid1);
    assertThat(emptyCashbackExpiries.size()).isEqualTo(0);
    emptyCashbackExpiries = cashbackExpiryRepository.cashbackExpiryOfPid(refundPid2);
    assertThat(emptyCashbackExpiries.size()).isEqualTo(0);
    emptyCashbackExpiries = cashbackExpiryRepository.cashbackExpiryOfPid(refundPid3);
    assertThat(emptyCashbackExpiries.size()).isEqualTo(0);
  }

  @Test
  public void merchantRefundSuccessExternal() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();

    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 400), Money.ZERO_JPY, txnId2, null);
    long newId = walletService.refund(request);

    Assert.assertEquals(beforeP - 400, merchantWallet.payable().balance().getCents().longValue());
    final List<DerivedTransaction> derivedTransactions = derivedTransactionRepository
        .transactionsOfOriginalPid(txnId2);
    Assert.assertEquals(1, derivedTransactions.size());
    Assert.assertEquals(String.valueOf(400), derivedTransactions.get(0).getMetadata().get("refunded"));
    Assert.assertEquals(txnId2, derivedTransactions.get(0).getOriginalPid());
    Assert.assertEquals(newId, derivedTransactions.get(0).getDerivedPid());
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void merchantRefundFailedWithNoEnoughMoney() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();
    walletService.batchRelease(new MerchantBatchReleaseDTO(merchantId, merchantWallet.payable().balance(), Money.ZERO_JPY, Money.ZERO_JPY, null));

    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 400), Money.ZERO_JPY, txnId2, null);
    walletService.refund(request);
  }

  @Test
  public void merchantRefundSuccessWalletIfBalanceIsNegative() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));
    walletService.batchRelease(new MerchantBatchReleaseDTO(merchantId, merchantWallet.payable().balance(), Money.ZERO_JPY, Money.ZERO_JPY, null));

    merchantWallet.setMpaLimit(-500);
    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 400), Money.ZERO_JPY, txnId2, null);
    walletService.refund(request);
    Assertions.assertThatThrownBy(()->walletService.refund(new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 200), Money.ZERO_JPY, txnId2, null)))
        .isInstanceOf(NotEnoughMoneyException.class);
  }

  @Test
  public void merchantRefundFailedIfBelowSystemLevelLimit() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 10000002);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));
    walletService.batchRelease(new MerchantBatchReleaseDTO(merchantId, merchantWallet.payable().balance(), Money.ZERO_JPY, Money.ZERO_JPY, null));

    merchantWallet.setMpaLimit(-20000000);
    Assertions.assertThatThrownBy(()->walletService.refund(new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 10000001), Money.ZERO_JPY, txnId2, null)))
        .isInstanceOf(NotEnoughMoneyException.class);
  }

  @Test(expected = CreditNotAllowedException.class)
  public void merchantRefundFailedForExcessiveAmount1() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();

    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 500), Money.ZERO_JPY, txnId2, null);
    walletService.refund(request);

    Assert.assertEquals(beforeP, merchantWallet.payable().balance().getCents().longValue());
  }

  @Test(expected = CreditNotAllowedException.class)
  public void merchantRefundFailedForExcessiveAmount2() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 1300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    long beforeP = merchantWallet.payable().balance().getCents();

    MerchantRefundDTO request = new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 1500), Money.ZERO_JPY, txnId1, new HashMap<>());
    walletService.refund(request);

    Assert.assertEquals(beforeP, merchantWallet.payable().balance().getCents().longValue());
  }

  @Test
  public void alipayPrepareThenCapture() {
    Money amount = new Money(Currency.JPY, 999l);
    long balance = merchantWallet.getTotalBalance();
    long txnId = walletService.alipayPrepare(new ThirdPartyPrepareDTO(merchantWallet.getUserId(), amount, null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId), null));
    assertThat(merchantWallet.getTotalBalance()).isEqualTo(balance + amount.getCents());
    assertThat(transactionRepository.transactionsOfPid(txnId).size()).isEqualTo(2);
  }

  @Test
  public void alipayPrepareThenCancel() {
    Money amount = new Money(Currency.JPY, 999l);
    long balance = merchantWallet.getTotalBalance();
    long txnId = walletService.alipayPrepare(new ThirdPartyPrepareDTO(merchantWallet.getUserId(), amount, null));
    walletService.paymentReverse(new CustomerReverseDTO(Arrays.asList(txnId), new HashMap<>()));
    assertThat(merchantWallet.getTotalBalance()).isEqualTo(balance);
    assertThat(transactionRepository.transactionsOfPid(txnId).size()).isEqualTo(2);
    assertThat(derivedTransactionRepository.transactionsOfOriginalPid(txnId).size()).isEqualTo(1);
  }

  @Test
  public void topupCustomerCampaign() {
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account systemCampaign = walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(
        UuidGenerator.getUID(),
        systemCampaign,
        new Money(Currency.JPY, 10000),
        new Money(Currency.JPY, 10000),
        null);

    String budgetToken = "budgetToken";
    HashMap<String, String> metadata = new HashMap<>();
    Money balance = new Money(Currency.JPY, 100);
    MacroTransaction macroTransaction = walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, balance, metadata));

    assertThat(walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId()).campaign().balance().getCents()).isEqualTo(0L);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(macroTransaction.getPid());
    Assertions.assertThat(transactions)
        .extracting("srcAccountUuid", "srcAccountType", "dstAccountUuid", "dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(systemCampaign.getId(), AccountType.CAMPAIGN, customerWallet.campaign().getId(), AccountType.CAMPAIGN, balance.getCents(), balance.getCents())
        );
  }

  @Test
  public void createMerchantTopupAccount() {
    CreateMerchantTopupAccountDTO createMerchantTopupAccountDTO = new CreateMerchantTopupAccountDTO(merchantId);
    Account createdAccount = walletService.createMerchantTopupAccount(createMerchantTopupAccountDTO);
    Account expectedAccount = new Account(merchantWallet,  AccountType.TOPUP, Currency.JPY, true, new HashMap<>());

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getType()).isEqualTo(AccountType.TOPUP);
    assertThat(createdAccount.wallet().getUuid()).isEqualTo(expectedAccount.wallet().getUuid());
  }

  @Test
  public void createMerchantCampaignAccount() {
    CreateMerchantCampaignAccountDTO dto = new CreateMerchantCampaignAccountDTO(merchantId, campaignToken, new HashMap<>());
    Account createdAccount = walletService.createMerchantCampaignAccount(dto);
    Account expectedAccount = new Account(merchantWallet,  AccountType.CAMPAIGN, Currency.JPY, true, new HashMap<>());

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getType()).isEqualTo(AccountType.CAMPAIGN);
    assertThat(createdAccount.wallet().getUuid()).isEqualTo(expectedAccount.wallet().getUuid());
    assertThat(createdAccount.getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN)).isEqualTo(campaignToken);
  }

  @Test
  public void updateMerchantCampaignAccountToAllowNegativeBalance(){

    String campaignToken = "NEGATIVE_CAMPAIGN_2019";
    CreateMerchantCampaignAccountDTO dto = new CreateMerchantCampaignAccountDTO(merchantId, campaignToken, new HashMap<>());
    Account campaign = walletService.createMerchantCampaignAccount(dto);
    campaign.setNegativeBalanceAllowed(true);
    campaign = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken).get();

    assertThat(campaign.balance().getCents()).isEqualTo(0);
    assertThat(campaign.getMetadataByKey("_negative_balance_allowed")).isEqualTo("true");
  }

  @Test
  public void balanceOfNegativeBalanceAllowedMerchantCampaignCanBeMinus(){

    updateMerchantCampaignAccountToAllowNegativeBalance();
    String campaignToken = "NEGATIVE_CAMPAIGN_2019";
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO request = new MerchantCampaignCashbackDepositDTO(merchantId, campaignToken, customerWallet.getUserId(), depositAmount, CASHBACK.name(), new HashMap<>());

    // can be minus from 0
    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(request);
    Account campaign = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken).get();
    assertThat(campaign.balance().getCents()).isEqualTo(-300);
    Account cashback = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId()).cashback();
    assertThat(cashback.balance().getCents()).isEqualTo(300);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.parseLong(response.getPid()));
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK, 300L, 300L, 0L, 300L)
        );

    // can be minus from minus
    response = walletService.processMerchantCampaignCashbackDeposit(request);
    campaign = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken).get();
    assertThat(campaign.balance().getCents()).isEqualTo(-600);
    cashback = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId()).cashback();
    assertThat(cashback.balance().getCents()).isEqualTo(600);
    transactions = transactionRepository.transactionsOfPid(Long.parseLong(response.getPid()));
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK, 300L, 300L, 0L, 600L)
        );
  }

  @Test
  public void merchantPayableFunding() {
    Account payableAccount = merchantWallet.payable();
    Money beforePayable = payableAccount.balance();
    assertThat(beforePayable.getCents()).isEqualTo(0L);

    Money payableFund = new Money(Currency.JPY, 100L);
    MerchantPayableFundingDTO dto = new MerchantPayableFundingDTO(merchantWallet.getUserId(), payableFund, null);
    Transaction t = walletService.merchantPayableAccountFunding(dto);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
            .containsExactly(
                    tuple(
                            AccountType.SOURCE, AccountType.PAYABLE,
                            payableFund.getCents(), payableFund.getCents(),
                            0L, beforePayable.getCents() + payableFund.getCents()) // payable account is included in merchant total balance.
            );

    Optional<Account> actual = accountRepository.accountOfId(payableAccount.getId());
    assertThat(actual.isPresent()).isEqualTo(true);
    assertThat(actual.get().balance()).isEqualToComparingFieldByField(payableFund.add(beforePayable));
  }

  @Test
  public void topupFunding() {
    Account topupAccount = walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantWallet.getUserId(), null));
    assertThat(topupAccount.balance().getCents()).isEqualTo(0);

    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupFundingDTO dto = new MerchantTopupFundingDTO(merchantWallet.getUserId(), amount, null);
    Transaction t = walletService.topupFunding(dto);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.SOURCE, AccountType.TOPUP, amount.getCents(), amount.getCents(), 0L, 0L)
        );

    Optional<Account> actual = accountRepository.accountOfId(topupAccount.getId());
    assertThat(actual.isPresent()).isEqualTo(true);
    assertThat(actual.get().balance()).isEqualToComparingFieldByField(amount);
  }

  @Test
  public void merchantCampaignFundingTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Account campaignAccount = walletService.createMerchantCampaignAccount((new CreateMerchantCampaignAccountDTO(merchantWallet.getUserId(), campaignToken,null)));
    assertThat(campaignAccount.balance().getCents()).isEqualTo(0);

    Money campaignFund = new Money(Currency.JPY, 100);
    MerchantCampaignTopupDTO merchantCampaignTopupDTO = new MerchantCampaignTopupDTO(campaignToken, merchantId, campaignFund, null );
    Transaction t = walletService.merchantCampaignAccountFunding((merchantCampaignTopupDTO));

    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.SOURCE, AccountType.CAMPAIGN, campaignFund.getCents(), campaignFund.getCents(), 0L, 0L)
        );

    Optional<Account> actual = accountRepository.accountOfId(campaignAccount.getId());
    assertThat(actual.isPresent()).isEqualTo(true);
    assertThat(actual.get().balance()).isEqualToComparingFieldByField(campaignFund);
  }

  @Test(expected = AccountNotFoundException.class)
  public void topupFundingNotFoundAccount() {
    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupFundingDTO dto = new MerchantTopupFundingDTO(merchantWallet.getUserId(), amount, null);
    walletService.topupFunding(dto);
  }

  @Test(expected = AccountNotFoundException.class)
  public void campaignFundingNotFoundAccountTest() {
    Money campaignFund = new Money(Currency.JPY, 100);
    MerchantCampaignTopupDTO merchantCampaignTopupDTO = new MerchantCampaignTopupDTO(campaignToken, merchantId, campaignFund, null );
    walletService.merchantCampaignAccountFunding((merchantCampaignTopupDTO));
  }

  @Test
  public void topupFundingReverse() {
    Account topupAccount = walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantWallet.getUserId(), null));
    assertThat(topupAccount.balance().getCents()).isEqualTo(0);

    // emoney the topup account
    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupFundingDTO dto = new MerchantTopupFundingDTO(merchantWallet.getUserId(), amount, null);
    Transaction t = walletService.topupFunding(dto);

    // topup account
    Optional<Account> afterFunding = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).topup();
    assertThat(afterFunding.get().balance()).isEqualToComparingFieldByField(amount);

    // reverse the topup funding
    walletService.merchantTopupFundingReverse(new ConfirmOrCancelDTO(t.getPid(), null));

    // assert that the topup account is 0
    Optional<Account>  afterReverse= walletFactory.rebuildMerchantWalletByOwnerId(merchantId).topup();
    assertThat(afterReverse.get().balance()).isEqualToComparingFieldByField(new Money(0));
  }

  @Test
  public void campaignFundingReverseTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Account campaignAccount = walletService.createMerchantCampaignAccount((new CreateMerchantCampaignAccountDTO(merchantWallet.getUserId(), campaignToken,null)));
    assertThat(campaignAccount.balance().getCents()).isEqualTo(0);

    // emoney the topup account
    Money campaignFund = new Money(Currency.JPY, 100);
    MerchantCampaignTopupDTO merchantCampaignTopupDTO = new MerchantCampaignTopupDTO(campaignToken, merchantId, campaignFund, null );
    Transaction t = walletService.merchantCampaignAccountFunding((merchantCampaignTopupDTO));

    // topup account
    Optional<Account> afterFunding = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken);
    assertThat(afterFunding.get().balance()).isEqualToComparingFieldByField(campaignFund);

    // reverse the topup funding
    walletService.merchantCampaignAccountFundReverse(new ConfirmOrCancelDTO(t.getPid(), null));

    // assert that the topup account is 0
    Optional<Account>  afterReverse= walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken);
    assertThat(afterReverse.get().balance()).isEqualToComparingFieldByField(new Money(0));
  }

  @Test(expected = AlreadyRevertedException.class)
  public void topupFundingReverseAlreadyReversed() {
    Account topupAccount = walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantWallet.getUserId(), null));
    assertThat(topupAccount.balance().getCents()).isEqualTo(0);

    // emoney the topup account
    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupFundingDTO dto = new MerchantTopupFundingDTO(merchantWallet.getUserId(), amount, null);
    Transaction t = walletService.topupFunding(dto);

    // topup account
    Optional<Account> afterFunding = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).topup();
    assertThat(afterFunding.get().balance()).isEqualToComparingFieldByField(amount);

    // reverse the topup funding
    walletService.merchantTopupFundingReverse(new ConfirmOrCancelDTO(t.getPid(), null));

    // assert that the topup account is 0
    Optional<Account>  afterReverse= walletFactory.rebuildMerchantWalletByOwnerId(merchantId).topup();
    assertThat(afterReverse.get().balance()).isEqualToComparingFieldByField(new Money(0));

    // revert again, should throw exception
    walletService.merchantTopupFundingReverse(new ConfirmOrCancelDTO(t.getPid(), null));
  }

  @Test(expected = AlreadyRevertedException.class)
  public void campaignFundingReverseAlreadyReversedTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Account campaignAccount = walletService.createMerchantCampaignAccount((new CreateMerchantCampaignAccountDTO(merchantWallet.getUserId(), campaignToken,null)));
    assertThat(campaignAccount.balance().getCents()).isEqualTo(0);

    // emoney the topup account
    Money campaignFund = new Money(Currency.JPY, 100);
    MerchantCampaignTopupDTO merchantCampaignTopupDTO = new MerchantCampaignTopupDTO(campaignToken, merchantId, campaignFund, null );
    Transaction t = walletService.merchantCampaignAccountFunding((merchantCampaignTopupDTO));

    // topup account
    Optional<Account> afterFunding = walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken);
    assertThat(afterFunding.get().balance()).isEqualToComparingFieldByField(campaignFund);

    // reverse the topup funding
    walletService.merchantCampaignAccountFundReverse(new ConfirmOrCancelDTO(t.getPid(), null));

    // assert that the topup account is 0
    Optional<Account>  afterReverse= walletFactory.rebuildMerchantWalletByOwnerId(merchantId).campaign(campaignToken);
    assertThat(afterReverse.get().balance()).isEqualToComparingFieldByField(new Money(0));

    // revert again, should throw exception
    walletService.merchantCampaignAccountFundReverse(new ConfirmOrCancelDTO(t.getPid(), null));
  }

  @Test(expected = TransactionNotFoundException.class)
  public void topupFundingReverseNotExistingTransaction() {
    ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(UuidGenerator.getUID(), null);
    walletService.merchantTopupFundingReverse(request);
  }

  @Test(expected = TransactionNotFoundException.class)
  public void campaignFundingReverseNotExistingTransactionTest() {
    ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(UuidGenerator.getUID(), null);
    walletService.merchantCampaignAccountFundReverse(request);
  }

  @Test(expected = AccountTypeNotAllowedException.class)
  public void topupFundingReverseAccountTypeNotAllowedTest(){

    // creating wrong transaction scenario
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Account campaignAccount = walletService.createMerchantCampaignAccount((new CreateMerchantCampaignAccountDTO(merchantWallet.getUserId(), campaignToken,null)));
    assertThat(campaignAccount.balance().getCents()).isEqualTo(0);

    Money campaignFund = new Money(Currency.JPY, 100);
    MerchantCampaignTopupDTO merchantCampaignTopupDTO = new MerchantCampaignTopupDTO(campaignToken, merchantId, campaignFund, null );
    Transaction wrongTransaction = walletService.merchantCampaignAccountFunding((merchantCampaignTopupDTO));


    walletService.merchantTopupFundingReverse(new ConfirmOrCancelDTO(wrongTransaction.getPid(), null));
  }

  @Test(expected = AccountTypeNotAllowedException.class)
  public void campaignFundingReverseAccountTypeNotAllowedTest(){

    // creating wrong transaction scenario
    Account topupAccount = walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantWallet.getUserId(), null));
    assertThat(topupAccount.balance().getCents()).isEqualTo(0);
    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupFundingDTO dto = new MerchantTopupFundingDTO(merchantWallet.getUserId(), amount, null);
    Transaction wrongTransaction = walletService.topupFunding(dto);


    walletService.merchantCampaignAccountFundReverse(new ConfirmOrCancelDTO(wrongTransaction.getPid(), null));
  }

  @Test
  public void merchantPrepaidDepositTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money cashback = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO request = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.PREPAID, cashback, new HashMap<>());

    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(request);
    Account campaignAccount = merchantWallet.campaign(campaignToken).get();
    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.parseLong(response.getPid()));
    Assertions.assertThat(transactions.size()).isEqualTo(1);
    Transaction depositTransaction = transactions.get(0);

    Assertions.assertThat(depositTransaction.getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    Assertions.assertThat(depositTransaction.getDstAccountType()).isEqualTo(AccountType.PREPAID);

    Assertions.assertThat(depositTransaction.getSrcAccountUuid()).isEqualTo(campaignAccount.getId());
    Assertions.assertThat(depositTransaction.getDstAccountUuid()).isEqualTo(customerWallet.prepaid().getId());

    Assertions.assertThat(depositTransaction.getSrcAmount()).isEqualTo(cashback.getCents());
    Assertions.assertThat(depositTransaction.getDstAmount()).isEqualTo(cashback.getCents());
  }

  @Test
  public void merchantCashbackDepositTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money cashback = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO request = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, cashback, new HashMap<>());

    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(request);
    Account campaignAccount = merchantWallet.campaign(campaignToken).get();
    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.parseLong(response.getPid()));
    Assertions.assertThat(transactions.size()).isEqualTo(1);
    Transaction depositTransaction = transactions.get(0);

    Assertions.assertThat(depositTransaction.getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    Assertions.assertThat(depositTransaction.getDstAccountType()).isEqualTo(AccountType.CASHBACK);

    Assertions.assertThat(depositTransaction.getSrcAccountUuid()).isEqualTo(campaignAccount.getId());
    Assertions.assertThat(depositTransaction.getDstAccountUuid()).isEqualTo(customerWallet.cashback().getId());

    Assertions.assertThat(depositTransaction.getSrcAmount()).isEqualTo(cashback.getCents());
    Assertions.assertThat(depositTransaction.getDstAmount()).isEqualTo(cashback.getCents());
  }

  @Test
  public void merchantCashbackPendingDepositTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money cashback = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    metadata.put("activation_date", DateUtils.dateToString(DateUtils.dateFromDays("4")));
    MerchantCampaignCashbackDepositDTO request = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, cashback, metadata);

    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(request);
    Account campaignAccount = merchantWallet.campaign(campaignToken).get();
    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.parseLong(response.getPid()));
    Assertions.assertThat(transactions.size()).isEqualTo(1);
    Transaction depositTransaction = transactions.get(0);

    Assertions.assertThat(depositTransaction.getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    Assertions.assertThat(depositTransaction.getDstAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);

    Assertions.assertThat(depositTransaction.getSrcAccountUuid()).isEqualTo(campaignAccount.getId());
    Assertions.assertThat(depositTransaction.getDstAccountUuid()).isEqualTo(customerWallet.cashbackPending().getId());

    Assertions.assertThat(depositTransaction.getSrcAmount()).isEqualTo(cashback.getCents());
    Assertions.assertThat(depositTransaction.getDstAmount()).isEqualTo(cashback.getCents());
  }

  @Test
  public void merchantCashbackExpirableDepositTest() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money cashback = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    MerchantCampaignCashbackDepositDTO request = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_EXPIRABLE, cashback, metadata);

    TransactionV2DTO response = walletService.processMerchantCampaignCashbackDeposit(request);
    Account campaignAccount = merchantWallet.campaign(campaignToken).get();
    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.parseLong(response.getPid()));
    Assertions.assertThat(transactions.size()).isEqualTo(1);
    Transaction depositTransaction = transactions.get(0);

    Assertions.assertThat(depositTransaction.getSrcAccountType()).isEqualTo(AccountType.CAMPAIGN);
    Assertions.assertThat(depositTransaction.getDstAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);

    Assertions.assertThat(depositTransaction.getSrcAccountUuid()).isEqualTo(campaignAccount.getId());
    Assertions.assertThat(depositTransaction.getDstAccountUuid()).isEqualTo(customerWallet.cashbackExpirable().getId());

    Assertions.assertThat(depositTransaction.getSrcAmount()).isEqualTo(cashback.getCents());
    Assertions.assertThat(depositTransaction.getDstAmount()).isEqualTo(cashback.getCents());

    List<CashbackExpiry> cashbackExpiries = cashbackExpiryRepository.cashbackExpiryOfPid(Long.parseLong(response.getPid()));
    Assertions.assertThat(cashbackExpiries.size()).isEqualTo(1);
    CashbackExpiry record = cashbackExpiries.get(0);

    Assertions.assertThat(record.getStatus()).isEqualTo(CashbackExpiryStatus.COMPLETED);
    Assertions.assertThat(record.getCashback().getCents()).isEqualToComparingFieldByField(cashback.getCents());
    Assertions.assertThat(record.getExpiryDate()).isEqualTo(expiryDate);
    Assertions.assertThat(record.getUserId()).isEqualTo(customerWallet.getUserId());
    Assertions.assertThat(record.getMetadata().get(MetaKeys.MERCHANT_ID)).isEqualTo(request.getMerchantWalletOwnerId());
    Assertions.assertThat(record.getMetadata().get(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(request.getCampaign_token());
    Assertions.assertThat(record.getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());
  }

  @Test(expected = InvalidCashbackRequestException.class)
  public void merchantCashbackNotPositiveTest() {

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money cashback = new Money(Currency.JPY, -300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    MerchantCampaignCashbackDepositDTO request = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, cashback, metadata);

    walletService.processMerchantCampaignCashbackDeposit(request);
  }

  @Test(expected = InvalidCashbackRequestException.class)
  public void merchantCashbackShouldFailWithInvalidAccountType() {
    String campaignToken = String.valueOf(UuidGenerator.getUID());

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("key", "value");
    MerchantCampaignCashbackDepositDTO request = new MerchantCampaignCashbackDepositDTO();
    request.setCampaign_token(campaignToken);
    request.setCustomer_wallet_owner_id(nonKycCustomerId);
    request.setAmount(amount);
    request.setAccountType("INVALID_ACCOUNT_TYPE");
    request.setMetadata(meta);
    walletService.cashbackV3(request);
  }

  @Test(expected = AccountNotFoundException.class)
  public void merchantCampaignAccountNotFound() {
    MerchantCampaignCashbackDepositDTO request = new MerchantCampaignCashbackDepositDTO();

    request.metadata = new HashMap<>();
    request.merchantWalletOwnerId = merchantId;
    request.customer_wallet_owner_id = customerWallet.getUserId();
    request.campaign_token = "invalid-token";
    request.setAccountType(AccountType.CASHBACK.name());
    request.amount = new Money(Currency.JPY, 100);

    walletService.processMerchantCampaignCashbackDeposit(request);
  }



  @Test
  public void fullMerchantCashbackReverseWithCashback() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());
    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackAccount = customerWallet.getCashback();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackAccount.getId()).get().balance().getCents();

    // Main
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(depositAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(depositAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CASHBACK, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(depositPid));

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackBalanceAfterReverse = accountRepository.accountOfId(customerCashbackAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + depositAmount.getCents());
    assertThat(customerCashbackBalanceAfterReverse).isEqualTo(customerCashbackBalanceBeforeReverse - depositAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void fullMerchantCashbackReverseWithCashbackAfterRelease() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    metadata.put("activation_date", DateUtils.dateToString(DateUtils.dateFromDays("4")));
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, depositAmount, metadata);
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(depositPid, new HashMap<>());
    long releasePid = walletService.cashbackReleaseV3(request);

    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackAccount = customerWallet.getCashback();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackAccount.getId()).get().balance().getCents();

    // Main
    assertThat(releasePid).isEqualTo(depositPid);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(depositAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(depositAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CASHBACK, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(depositPid));
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackBalanceAfterReverse = accountRepository.accountOfId(customerCashbackAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + depositAmount.getCents());
    assertThat(customerCashbackBalanceAfterReverse).isEqualTo(customerCashbackBalanceBeforeReverse - depositAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void fullMerchantCashbackReverseWithCashbackPending() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    metadata.put("activation_date", DateUtils.dateToString(DateUtils.dateFromDays("4")));
    metadata.put("attributed_campaign_id", "123");
    metadata.put("registration_id", "1234");
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, depositAmount, metadata);
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());
    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackPendingAccount = customerWallet.getCashbackPending();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackPendingBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackPendingAccount.getId()).get().balance().getCents();

    // Main
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(depositAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(depositAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK_PENDING, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L),
            tuple(AccountType.CASHBACK_PENDING, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    assertThat(transactions.get(1).getMetadataByKey("attributed_campaign_id")).isEqualTo("123");
    assertThat(transactions.get(1).getMetadataByKey("registration_id")).isEqualTo("1234");
    assertThat(transactions.get(1).getMetadataByKey("merchant_id")).isEqualTo("merchant");

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions.size()).isEqualTo(0);

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackPendingBalanceAfterReverse = accountRepository.accountOfId(customerCashbackPendingAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + depositAmount.getCents());
    assertThat(customerCashbackPendingBalanceAfterReverse).isEqualTo(customerCashbackPendingBalanceBeforeReverse - depositAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void fullMerchantCashbackReverseWithCashbackExpirable() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    metadata.put("attributed_campaign_id", "123");
    metadata.put("registration_id", "1234");
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_EXPIRABLE, depositAmount, metadata);
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());
    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackExpirableAccount = customerWallet.getCashbackExpirable();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackExpirableBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackExpirableAccount.getId()).get().balance().getCents();

    // Main
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(depositAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(depositAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    assertThat(transactions.get(0).getMetadataByKey("attributed_campaign_id")).isEqualTo("123");
    assertThat(transactions.get(0).getMetadataByKey("registration_id")).isEqualTo("1234");

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackExpirableBalanceAfterReverse = accountRepository.accountOfId(customerCashbackExpirableAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + depositAmount.getCents());
    assertThat(customerCashbackExpirableBalanceAfterReverse).isEqualTo(customerCashbackExpirableBalanceBeforeReverse - depositAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            tuple(depositPid, depositAmount, customerWallet.getUserId(), CashbackExpiryStatus.REVERSED)
        );
  }

  @Test
  public void fullMerchantCashbackReverseWithCashbackExpirableAfterRelease() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, depositAmount, metadata);
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(depositPid, new HashMap<>());
    long releasePid = walletService.cashbackReleaseV3(request);

    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackExpirableAccount = customerWallet.getCashbackExpirable();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackExpirableBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackExpirableAccount.getId()).get().balance().getCents();

    // Main
    assertThat(releasePid).isEqualTo(depositPid);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(depositAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(depositAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackExpirableBalanceAfterReverse = accountRepository.accountOfId(customerCashbackExpirableAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + depositAmount.getCents());
    assertThat(customerCashbackExpirableBalanceAfterReverse).isEqualTo(customerCashbackExpirableBalanceBeforeReverse - depositAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            tuple(depositPid, depositAmount, customerWallet.getUserId(), CashbackExpiryStatus.REVERSED)
        );
  }

  @Test(expected = AlreadyRevertedException.class)
  public void fullMerchantCashbackReverseAlreadyReverted() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    // Main
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);
    List<Transaction> newTxn = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(newTxn.size()).isEqualTo(1);
    walletService.merchantCashbackReverse(reverseRequest);
  }

  @Test
  public void partialMerchantCashbackReverseWithCashback() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());
    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackAccount = customerWallet.getCashback();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackAccount.getId()).get().balance().getCents();

    // Main
    Money partialReverseAmount = new Money(Currency.JPY, 100);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, partialReverseAmount, new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(partialReverseAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(partialReverseAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CASHBACK, AccountType.CAMPAIGN, partialReverseAmount.getCents(), partialReverseAmount.getCents(), depositAmount.getCents() - partialReverseAmount.getCents(), 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(0).getMetadataByKey("_original_pid")).isEqualTo(String.valueOf(depositPid));

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackBalanceAfterReverse = accountRepository.accountOfId(customerCashbackAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + partialReverseAmount.getCents());
    assertThat(customerCashbackBalanceAfterReverse).isEqualTo(customerCashbackBalanceBeforeReverse - partialReverseAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void partialMerchantCashbackReverseWithCashbackPending() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    metadata.put("activation_date", DateUtils.dateToString(DateUtils.dateFromDays("4")));
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_PENDING, depositAmount, metadata);
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());
    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackPendingAccount = customerWallet.getCashbackPending();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackPendingBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackPendingAccount.getId()).get().balance().getCents();

    // Main
    Money partialReverseAmount = new Money(Currency.JPY, 100);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, partialReverseAmount, new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNotNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(partialReverseAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_PENDING);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(partialReverseAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK_PENDING, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L),
            tuple(AccountType.CASHBACK_PENDING, AccountType.CAMPAIGN, depositAmount.getCents(), depositAmount.getCents(), 0L, 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(1).getMetadataByKey(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(transactions.get(1).getMetadataByKey("merchant_name")).isNull();
    long partialDepositCents = depositAmount.getCents() - partialReverseAmount.getCents();
    transactions = transactionRepository.transactionsOfPid(reverseResponse.getPartialTransactionPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CAMPAIGN, AccountType.CASHBACK_PENDING, partialDepositCents, partialDepositCents, 0L, 0L)
        );
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPartialTransactionPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackPendingBalanceAfterReverse = accountRepository.accountOfId(customerCashbackPendingAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + partialReverseAmount.getCents());
    assertThat(customerCashbackPendingBalanceAfterReverse).isEqualTo(customerCashbackPendingBalanceBeforeReverse - partialReverseAmount.getCents());

    List<CashbackExpiry> cashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(cashbackExpiry.size()).isEqualTo(0);
  }

  @Test
  public void partialMerchantCashbackReverseWithCashbackExpirable() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    Map<String, String> metadata = new HashMap<>();
    Date expiryDate = DateUtils.dateFromDays("6");
    metadata.put("expiry_date", DateUtils.dateToString(expiryDate));
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK_EXPIRABLE, depositAmount, metadata);
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());
    Account merchantCampaignAccount = merchantWallet.campaign(campaignToken).get();
    Account customerCashbackExpirableAccount = customerWallet.getCashbackExpirable();
    long merchantCampaignBalanceBeforeReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackExpirableBalanceBeforeReverse = accountRepository.accountOfId(customerCashbackExpirableAccount.getId()).get().balance().getCents();

    // Main
    Money partialReverseAmount = new Money(Currency.JPY, 100);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, partialReverseAmount, new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    assertThat(reverseResponse.getPid()).isNotEqualTo(depositPid);
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount()).isEqualTo(partialReverseAmount);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK_EXPIRABLE);
    assertThat(reverseResponse.getReversed().get(0).getAmount()).isEqualTo(partialReverseAmount);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.CASHBACK_EXPIRABLE, AccountType.CAMPAIGN, partialReverseAmount.getCents(), partialReverseAmount.getCents(),
                                                                              depositAmount.getCents() - partialReverseAmount.getCents(), 0L)
        );
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(transactions.get(0).getMetadataByKey("merchant_name")).isEqualTo("PayPay Store");
    assertThat(transactions.get(0).getMetadataByKey(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(depositPid);
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type")
        .containsExactly(
            tuple(depositPid, reverseResponse.getPid(), DerivedTransactionType.REVERT)
        );

    long merchantCampaignBalanceAfterReverse = accountRepository.accountOfId(merchantCampaignAccount.getId()).get().balance().getCents();
    long customerCashbackExpirableBalanceAfterReverse = accountRepository.accountOfId(customerCashbackExpirableAccount.getId()).get().balance().getCents();
    assertThat(merchantCampaignBalanceAfterReverse).isEqualTo(merchantCampaignBalanceBeforeReverse + partialReverseAmount.getCents());
    assertThat(customerCashbackExpirableBalanceAfterReverse).isEqualTo(customerCashbackExpirableBalanceBeforeReverse - partialReverseAmount.getCents());

    List<CashbackExpiry> originalCashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(depositPid);
    assertThat(originalCashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            tuple(depositPid, depositAmount, customerWallet.getUserId(), CashbackExpiryStatus.REVERSED)
        );
    assertThat(originalCashbackExpiry.get(0).getMetadata().get(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(originalCashbackExpiry.get(0).getMetadata().get(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    assertThat(originalCashbackExpiry.get(0).getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());
    List<CashbackExpiry> partialCashbackExpiry = cashbackExpiryRepository.cashbackExpiryOfPid(reverseResponse.getPid());
    assertThat(partialCashbackExpiry).extracting("pid", "cashback", "userId", "status")
        .containsExactly(
            tuple(reverseResponse.getPid(), new Money(depositAmount.getCents() - partialReverseAmount.getCents()), customerWallet.getUserId(), CashbackExpiryStatus.COMPLETED)
        );
    assertThat(partialCashbackExpiry.get(0).getMetadata().get(MetaKeys.MERCHANT_ID)).isEqualTo(merchantWallet.userId);
    assertThat(partialCashbackExpiry.get(0).getMetadata().get(MetaKeys.CAMPAIGN_TOKEN_SNAKE)).isEqualTo(campaignToken);
    assertThat(partialCashbackExpiry.get(0).getMetadata().get(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE)).isEqualTo(ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());
    assertThat(partialCashbackExpiry.get(0).getMetadata().get(MetaKeys.ORDER_ID)).isEqualTo(orderId);

  }

  @Test
  public void merchantCashbackReverseUseCashbackExpirableFirst() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 300);
    funding(AccountType.PREPAID, 400);
    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();

    Assert.assertEquals(200, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(300, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 200);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    Assert.assertEquals(200, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(500, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());

    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(500, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());
  }

  @Test
  public void merchantCashbackReverseUseCashbackSecond() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 300);
    funding(AccountType.PREPAID, 400);
    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();

    Assert.assertEquals(200, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(300, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    Assert.assertEquals(200, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(600, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());

    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(500, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());
  }

  @Test
  public void merchantCashbackReverseUsePrepaidThird() {

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 500);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId,campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    Money paymentAmount = new Money(Currency.JPY, 400 );
    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantId, paymentAmount, null));

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.PREPAID, 400);

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();

    Assert.assertEquals(200, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(100, cashback.balance().getCents().longValue());
    Assert.assertEquals(400, prepaid.balance().getCents().longValue());

    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0, cashback.balance().getCents().longValue());
    Assert.assertEquals(200, prepaid.balance().getCents().longValue());
  }

  @Test
  public void merchantCashbackReverseIsBestEffortAPI() {

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 500);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    Money paymentAmount = new Money(Currency.JPY, 400 );
    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantId, paymentAmount, null));

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(100, cashback.balance().getCents().longValue());
    Assert.assertEquals(0, prepaid.balance().getCents().longValue());

    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0, cashback.balance().getCents().longValue());
    Assert.assertEquals(0, prepaid.balance().getCents().longValue());

    assertThat(reverseResponse.getTotalReversedAmount().getCents()).isEqualTo(100);
    assertThat(reverseResponse.getReversed().size()).isEqualTo(1);
    assertThat(reverseResponse.getReversed().get(0).getAccountType()).isEqualTo(AccountType.CASHBACK);
    assertThat(reverseResponse.getReversed().get(0).getAmount().getCents()).isEqualTo(100);
  }

  @Test
  public void zeroMerchantCashbackReverse() {

    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 500);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    Money paymentAmount = new Money(Currency.JPY, 500 );
    walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantId, paymentAmount, null));

    Account cashbackExpirable = customerWallet.findAccountByType(AccountType.CASHBACK_EXPIRABLE).get();
    Account cashback = customerWallet.findAccountByType(AccountType.CASHBACK).get();
    Account prepaid = customerWallet.findAccountByType(AccountType.PREPAID).get();

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0, cashback.balance().getCents().longValue());
    Assert.assertEquals(0, prepaid.balance().getCents().longValue());

    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, depositRequest.getAmount(), new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);

    Assert.assertEquals(0, cashbackExpirable.balance().getCents().longValue());
    Assert.assertEquals(0, cashback.balance().getCents().longValue());
    Assert.assertEquals(0, prepaid.balance().getCents().longValue());

    assertThat(reverseResponse.getPid()).isNull();
    assertThat(reverseResponse.getPartialTransactionPid()).isNull();
    assertThat(reverseResponse.getTotalReversedAmount().getCents()).isEqualTo(0);
    assertThat(reverseResponse.getReversed()).isNull();
  }

  @Test(expected = AlreadyRevertedException.class)
  public void multiplePartialMerchantCashbackReverse() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    // Main
    Money partialReverseAmount = new Money(Currency.JPY, 100);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, partialReverseAmount, new HashMap<>());
    MerchantCashbackReverseResponseDTO reverseResponse = walletService.merchantCashbackReverse(reverseRequest);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(reverseResponse.getPid());
    assertThat(transactions.size()).isEqualTo(1);
    walletService.merchantCashbackReverse(reverseRequest);
  }

  @Test(expected = InvalidCashbackRequestException.class)
  public void merchantCashbackReverseOverDepositAmount() {
    // Setup
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    Money depositAmount = new Money(Currency.JPY, 300);
    MerchantCampaignCashbackDepositDTO depositRequest = getMerchantCashbackDepositRequest(merchantId, campaignToken, AccountType.CASHBACK, depositAmount, new HashMap<>());
    long depositPid = Long.parseLong(walletService.processMerchantCampaignCashbackDeposit(depositRequest).getPid());

    // Main
    Money reverseAmount = new Money(Currency.JPY, 301);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, reverseAmount, new HashMap<>());
    walletService.merchantCashbackReverse(reverseRequest);
  }

  @Test(expected = AlreadyExpiredException.class)
  public void merchantCashbackReverseShouldFailWithAlreadyExpired() {
    // Setup

    // fund merchant campaign account with 10000
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // expirable cashbacks deposit with expiry_date
    Date expiry_date = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date));
    long depositPid = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 100), metadata);
    // verify account balance after cashback deposit
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    MerchantWallet mwallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantIdWithOneCampaign);
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(100L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(100L);
    assertThat(mwallet.campaign(campaignToken).get().balance().getCents()).isEqualTo(9900);

    // expire/prepare
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date), new HashMap<>());
    List<TransactionV2DTO> preparePids = walletService.expireCashbackPrepare(prepare);
    // verify account balance after expire/prepare
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(100L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);

    // expire/confirm
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(0).getPid()), new HashMap<>());
    TransactionV2DTO confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(0).getPid()).isEqualTo(confirmPid.getPid());
    // verify account balance after expiry/confirm
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    mwallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantIdWithOneCampaign);
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);
    assertThat(mwallet.campaign(campaignToken).get().balance().getCents()).isEqualTo(10000);

    // Main
    Money reverseAmount = new Money(Currency.JPY, 100);
    CustomerCashbackReverseDTO reverseRequest = new CustomerCashbackReverseDTO(depositPid, reverseAmount, new HashMap<>());
    walletService.merchantCashbackReverse(reverseRequest);
  }

  @Test
  public void merchantTopupPrepare() {

    topupFunding();

    long beforeMerchantTopup = merchantWallet.topup().get().balance().getCents();
    long beforeCustomerIncoming = customerWallet.incoming().balance().getCents();

    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupPrepareDTO dto = new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), amount, merchantMeta);
    MacroTransaction macroTxns = walletService.merchantTopupPrepare(dto);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(macroTxns.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
        .containsExactly(
            tuple(AccountType.TOPUP, AccountType.INCOMING, amount.getCents(), amount.getCents(), 0L, 0L)
        );

    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(beforeMerchantTopup - amount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(beforeCustomerIncoming + amount.getCents());
  }

  @Test(expected = AccountNotFoundException.class)
  public void merchantTopupPrepareNotFoundAccount() {
    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupPrepareDTO dto = new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), amount, merchantMeta);
    walletService.merchantTopupPrepare(dto);
  }

  @Test
  public void merchantTopupPrepareThenConfirm(){

    // add topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // source --> topup
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));

    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);

    // topup --> incoming
    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents() - prepareAmount.getCents()); // should be 50
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

    // incoming --> prepaid
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0L
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L); // should be 0
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

  }

  @Test
  public void merchantTopupPrepareThenCancel(){

    // create topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // funding
    Money fundingAmount = new Money(Currency.JPY, 200);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, fundingAmount, null));

    // prepare
    Money prepareAmount = new Money(Currency.JPY, 150);
    long pid =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta)).getPid();
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents() - prepareAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // cancel
    walletService.merchantTopupCancel(new ConfirmOrCancelDTO(pid, null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents());
  }

  @Test
  public void merchantTopupPrepareWithRemovedBudgetConstraint() {
    topupFunding();
    merchantWallet.topup().get().setBudgetConstraintDisabled(true);

    long beforeMerchantTopup = merchantWallet.topup().get().balance().getCents();
    long beforeCustomerIncoming = customerWallet.incoming().balance().getCents();

    Money amount = new Money(Currency.JPY, 100);
    MerchantTopupPrepareDTO dto = new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), amount, merchantMeta);
    MacroTransaction macroTxns = walletService.merchantTopupPrepare(dto);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(macroTxns.getPid());
    assertThat(transactions).extracting("srcAccountType", "dstAccountType", "srcAmount", "dstAmount", "srcBalance", "dstBalance")
            .containsExactly(
                    tuple(AccountType.TOPUP, AccountType.INCOMING, amount.getCents(), amount.getCents(), 0L, 0L)
            );

    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(beforeMerchantTopup); // not updated
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(beforeCustomerIncoming + amount.getCents());
  }

  @Test
  public void merchantTopupPrepareThenConfirmWithRemovedBudgetConstraint(){
    // add topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // source --> topup
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));

    merchantWallet.topup().get().setBudgetConstraintDisabled(true);

    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);

    // topup --> incoming
    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents()); // not updated
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

    // incoming --> prepaid
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0L
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L); // should be 0
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

  }

  @Test
  public void merchantTopupPrepareThenCancelWithRemovedBudgetConstraint(){
    // create topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // funding
    Money fundingAmount = new Money(Currency.JPY, 200);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, fundingAmount, null));

    merchantWallet.topup().get().setBudgetConstraintDisabled(true);

    // prepare
    Money prepareAmount = new Money(Currency.JPY, 150);
    long pid =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta)).getPid();
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents()); // not updated
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // cancel
    walletService.merchantTopupCancel(new ConfirmOrCancelDTO(pid, null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents()); // not updated
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void merchantTopupConfirmedAlreadyCanceled(){

    // create topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // funding
    Money fundingAmount = new Money(Currency.JPY, 200);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, fundingAmount, null));

    // prepare
    Money prepareAmount = new Money(Currency.JPY, 150);
    long pid =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta)).getPid();
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents() - prepareAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // cancel
    walletService.merchantTopupCancel(new ConfirmOrCancelDTO(pid, null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents());

    // confirm
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(pid, null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void merchantTopupCancelAlreadyConfirmed(){

    // create topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // funding
    Money fundingAmount = new Money(Currency.JPY, 200);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, fundingAmount, null));

    // prepare
    Money prepareAmount = new Money(Currency.JPY, 150);
    long pid =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta)).getPid();
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents() - prepareAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // confirm
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(pid, null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // cancel
    walletService.merchantTopupCancel(new ConfirmOrCancelDTO(pid, null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void merchantTopupConfirmAlreadyConfirmed(){

    // create topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // funding
    Money fundingAmount = new Money(Currency.JPY, 200);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, fundingAmount, null));

    // prepare
    Money prepareAmount = new Money(Currency.JPY, 150);
    long pid =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta)).getPid();
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents() - prepareAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // confirm
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(pid, null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // confirm
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(pid, null));
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void merchantTopupCancelAlreadyCanceled(){

    // create topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));

    // funding
    Money fundingAmount = new Money(Currency.JPY, 200);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, fundingAmount, null));

    // prepare
    Money prepareAmount = new Money(Currency.JPY, 150);
    long pid =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta)).getPid();
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents() - prepareAmount.getCents());
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(prepareAmount.getCents());

    // cancel
    walletService.merchantTopupCancel(new ConfirmOrCancelDTO(pid, null));
    assertThat(customerWallet.incoming().balance().getCents()).isEqualTo(0L);
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(fundingAmount.getCents());

    // cancel
    walletService.merchantTopupCancel(new ConfirmOrCancelDTO(pid, null));
  }

  @Test
  public void merchantTopupConfirmThenRevertWithNonKycUser() {
    // add topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L);

    // source --> topup
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));
    // topup funded with  200
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents());

    // topup --> incoming
    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents() - prepareAmount.getCents()); // should be 50

    // incoming --> prepaid
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0L
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

    // prepaid --> topup
    walletService.merchantTopupRevert(new RevertDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents()); // should be 200
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Test
  public void merchantTopupConfirmThenRevertWithKycUser() {
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));

    Account topup = merchantWallet.topup().get();
    Account emoney = kycCustomerWallet.emoney().get();
    long originalCustomerBalance = kycCustomerWallet.getTotalBalance();
    long originalMerchantBalance = merchantWallet.getTotalBalance();

    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), kycCustomerWallet.getUserId(), prepareAmount, merchantMeta));
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(topupPrepareMacroTransaction.getPid(), null));


    MacroTransaction macroTransaction = walletService.merchantTopupRevert(new RevertDTO(topupPrepareMacroTransaction.getPid(), null));

    // verify wallet balance
    assertThat(originalCustomerBalance).isEqualTo(kycCustomerWallet.getTotalBalance());

    // verify transactions
    List<Transaction> transactions = macroTransaction.getTransactions();
    assertThat(transactions).extracting("srcUserId","srcAccountUuid","srcAmount","srcBalance","srcAccountType",
        "dstUserId","dstAccountUuid","dstAmount", "dstBalance", "dstAccountType").containsExactly(
        // revert(emoney -> merchant/topup)
        tuple(kycCustomerWallet.getUserId(), emoney.getId(), prepareCents, originalCustomerBalance, EMONEY,
            merchantWallet.getUserId(), topup.getId(), prepareCents, originalMerchantBalance, TOPUP)
    );

    long derivedPid = transactions.get(0).getPid();
    // verify derived transaction
    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(topupPrepareMacroTransaction.getPid());
    assertThat(derivedTransactions).extracting("originalPid", "derivedPid", "type").containsExactly(
        tuple(topupPrepareMacroTransaction.getPid(), derivedPid, DerivedTransactionType.REVERT)
    );
  }

  @Test
  public void merchantTopupConfirmThenRevertWithRemovedBudgetConstraint() {
    // add topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L);

    merchantWallet.topup().get().setBudgetConstraintDisabled(true);

    // source --> topup
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));
    // topup funded with  200
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L); // not updated

    // topup --> incoming
    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L); // not updated

    // incoming --> prepaid
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0L
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

    // prepaid --> topup
    walletService.merchantTopupRevert(new RevertDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L); // not updated
  }

  @Test
  public void merchantTopupConfirmThenRevertAlreadyReverted() {
    // add topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L);

    // source --> topup
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));
    // topup funded with  200
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents());

    // topup --> incoming
    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents() - prepareAmount.getCents()); // should be 50

    // incoming --> prepaid
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0L
    walletService.merchantTopupConfirm(new ConfirmOrCancelDTO(topupPrepareMacroTransaction.getPid(), null));
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(prepareAmount.getCents()); // should be 150

    // prepaid --> topup
    RevertDTO revertDTORequest = new RevertDTO(topupPrepareMacroTransaction.getPid(), null);
    walletService.merchantTopupRevert(revertDTORequest);
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(0L); // should be 0
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents()); // should be 200

    try {
      walletService.merchantTopupRevert(revertDTORequest);
    } catch(Exception e) {
      assertThat(e).isExactlyInstanceOf(MacroTxnStateMismatchException.class);
      Assert.assertEquals(e.getMessage(),(String.format("%d being full reverted", topupPrepareMacroTransaction.getPid())));
    }
  }

  @Test
  public void merchantTopupPrepareThenRevert() {
    // add topup account
    walletService.createMerchantTopupAccount(new CreateMerchantTopupAccountDTO(merchantId));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(0L);

    // source --> topup
    long topupCents = 200;
    Money topupAmount = new Money(Currency.JPY, topupCents);
    walletService.topupFunding(new MerchantTopupFundingDTO(merchantId, topupAmount, null));
    // topup funded with  200
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents());

    // topup --> incoming
    long prepareCents = 150;
    Money prepareAmount = new Money(Currency.JPY, prepareCents);
    MacroTransaction topupPrepareMacroTransaction =  walletService.merchantTopupPrepare(new MerchantTopupPrepareDTO(merchantWallet.getUserId(), customerWallet.getUserId(), prepareAmount, merchantMeta));
    assertThat(merchantWallet.topup().get().balance().getCents()).isEqualTo(topupAmount.getCents() - prepareAmount.getCents()); // should be 50

    try {
      RevertDTO revertDTORequest = new RevertDTO(topupPrepareMacroTransaction.getPid(), null);
      walletService.merchantTopupRevert(revertDTORequest);
    } catch(Exception e) {
      assertThat(e).isExactlyInstanceOf(MacroTxnStateMismatchException.class);
      Assert.assertEquals(e.getMessage(),(String.format("%d not in completed state", topupPrepareMacroTransaction.getPid())));
    }
  }

  @Test
  public void merchantTopupRevertNonExistingTransaction() {
    RevertDTO revertDTORequest = new RevertDTO(UuidGenerator.getUID(), null);
    try {
      walletService.merchantTopupRevert(revertDTORequest);
    } catch(Exception e) {
      assertThat(e).isExactlyInstanceOf(NoSuchMacroTxnException.class);
      Assert.assertEquals(e.getMessage(),(String.format("empty or null transaction for pid: %d", revertDTORequest.pid)));
    }
  }

  private List<MerchantReleaseDTO> release700YenSetup() {

    funding(AccountType.CASHBACK_EXPIRABLE, 200);
    funding(AccountType.CASHBACK, 1000);
    funding(AccountType.PREPAID, 800);

    Money dummy = new Money(Currency.JPY, 0L);
    Money amount = new Money(Currency.JPY, 300L);
    Money amount2 = new Money(Currency.JPY, 400L);

    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount, null));
    long txnId2 = walletService.prepare(new CustomerPrepareDTO(customerWallet.getUserId(), merchantWallet.getUserId(), amount2,null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1, txnId2), null));

    List<MerchantReleaseDTO> requests = Arrays.asList(
        new MerchantReleaseDTO(String.valueOf(merchantWallet.getUserId()), txnId1, dummy, dummy, null ),
        new MerchantReleaseDTO(String.valueOf(merchantWallet.getUserId()), txnId2, dummy, dummy, null ));

    walletService.release(requests);

    return requests;
  }

  @Test
  public void customerCampaignDeposit() {

    // Setup System's CAMPAIGN account
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account systemCampaign = walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), systemCampaign, new Money(Currency.JPY, 10000), new Money(Currency.JPY, 10000), null);

    // Setup Customer's CAMPAIGN account
    String budgetToken = "budgetToken";
    HashMap<String, String> metadata = new HashMap<>();
    Money topupAmount = new Money(Currency.JPY, 10000);
    walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, topupAmount, metadata));

    // Main
    CustomerWallet senderWallet = customerWallet;
    CustomerWallet receiverWallet = customerWallet1;
    Money depositAmount = new Money(Currency.JPY, 5000);
    metadata = new HashMap<>();
    MacroTransaction t = walletService.campaignDeposit(
        new CampaignDepositDTO(budgetToken, senderWallet.getUserId(), receiverWallet.getUserId(), depositAmount, metadata));
    receiverWallet = walletFactory.rebuildCustomerWalletByOwnerId(receiverWallet.getUserId());
    assertThat(receiverWallet.campaign().balance().getCents()).isEqualTo(0L);
    assertThat(receiverWallet.cashback().balance().getCents()).isEqualTo(depositAmount.getCents());
    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    Assertions.assertThat(transactions)
        .extracting("srcAccountUuid","srcAccountType","dstAccountUuid","dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(senderWallet.campaign().getId(), AccountType.CAMPAIGN, receiverWallet.cashback().getId(), AccountType.CASHBACK, depositAmount.getCents(), depositAmount.getCents())
        );
  }

  @Test
  public void customerCampaignDepositToCampaignOwnerSelf() {

    // Setup System's CAMPAIGN account
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account systemCampaign = walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), systemCampaign, new Money(Currency.JPY, 10000), new Money(Currency.JPY, 10000), null);

    // Setup Customer's CAMPAIGN account
    String budgetToken = "budgetToken";
    HashMap<String, String> metadata = new HashMap<>();
    Money topupAmount = new Money(Currency.JPY, 10000);
    walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, topupAmount, metadata));

    // Main
    CustomerWallet senderWallet = customerWallet;
    Money depositAmount = new Money(Currency.JPY, 5000);
    metadata = new HashMap<>();
    MacroTransaction t = walletService.campaignDeposit(
        new CampaignDepositDTO(budgetToken, senderWallet.getUserId(), senderWallet.getUserId(), depositAmount, metadata));
    senderWallet = walletFactory.rebuildCustomerWalletByOwnerId(senderWallet.getUserId());
    assertThat(senderWallet.campaign().balance().getCents()).isEqualTo(0L);
    assertThat(senderWallet.cashback().balance().getCents()).isEqualTo(depositAmount.getCents());
    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    Assertions.assertThat(transactions)
        .extracting("srcAccountUuid","srcAccountType","dstAccountUuid","dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(senderWallet.campaign().getId(), AccountType.CAMPAIGN, senderWallet.cashback().getId(), AccountType.CASHBACK, depositAmount.getCents(), depositAmount.getCents())
        );
  }

  @Test
  public void customerCampaignFundingReversal(){
    // Setup System's CAMPAIGN account
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account systemCampaign = walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), systemCampaign, new Money(Currency.JPY, 10000), new Money(Currency.JPY, 10000), null);

    // Setup Customer's CAMPAIGN account
    String budgetToken = "budgetToken";
    HashMap<String, String> metadata = new HashMap<>();
    Money topupAmount = new Money(Currency.JPY, 10000);
    MacroTransaction macroTransaction = walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, topupAmount, metadata));

    // Main
    metadata = new HashMap<>();
    CampaignTopupReverseDTO request = new CampaignTopupReverseDTO(macroTransaction.getPid(), budgetToken, topupAmount, metadata);
    MacroTransaction t = walletService.topupCustomerCampaignReverse(request);
    CustomerWallet afterReversedCustomerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(afterReversedCustomerWallet.campaign().balance().getCents()).isEqualTo(0L);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(t.getPid());
    Assertions.assertThat(transactions)
        .extracting("srcAccountUuid","srcAccountType","dstAccountUuid","dstAccountType", "srcAmount", "dstAmount")
        .containsExactly(
            tuple(systemCampaign.getId(), AccountType.CAMPAIGN, customerWallet.campaign().getId(), AccountType.CAMPAIGN, topupAmount.getCents(), topupAmount.getCents()),
            tuple(customerWallet.campaign().getId(), AccountType.CAMPAIGN, systemCampaign.getId(), AccountType.CAMPAIGN, topupAmount.getCents(), topupAmount.getCents())
        );
  }

  @Test(expected = MacroTxnStateMismatchException.class)
  public void customerCampaignFundingAlreadyReversal(){
    // Setup System's CAMPAIGN account
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account systemCampaign = walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), systemCampaign, new Money(Currency.JPY, 10000), new Money(Currency.JPY, 10000), null);

    // Setup Customer's CAMPAIGN account
    String budgetToken = "budgetToken";
    HashMap<String, String> metadata = new HashMap<>();
    Money topupAmount = new Money(Currency.JPY, 10000);
    MacroTransaction macroTransaction = walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, topupAmount, metadata));

    // Main
    metadata = new HashMap<>();
    CampaignTopupReverseDTO request = new CampaignTopupReverseDTO(macroTransaction.getPid(), budgetToken, topupAmount, metadata);
    // reversing first time
    walletService.topupCustomerCampaignReverse(request);
    // reversing second time
    walletService.topupCustomerCampaignReverse(request);
  }

  @Test(expected = BudgetMismatchException.class)
  public void customerCampaignFundingReverseWithNotFundedBudgettoken() {

    // Setup System's CAMPAIGN account
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account systemCampaign = walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), systemCampaign, new Money(Currency.JPY, 10000), new Money(Currency.JPY, 10000), null);

    // Setup Customer's CAMPAIGN account
    String budgetToken = "budgetToken";
    HashMap<String, String> metadata = new HashMap<>();
    Money topupAmount = new Money(Currency.JPY, 10000);
    MacroTransaction macroTransaction = walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, topupAmount, metadata));

    // Main
    metadata = new HashMap<>();
    CampaignTopupReverseDTO request = new CampaignTopupReverseDTO(macroTransaction.getPid(), "not_funded_budget_token", topupAmount, metadata);
    walletService.topupCustomerCampaignReverse(request);
  }

  @Test
  public void reverseCommission() {
    // prepare
    Account payable = merchantWallet.payable();
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), payable, new Money(1000), new Money(1000), null);

    Account commissionAccount = walletFactory.rebuildSystemWallet().getMerchantCommision();
    source.transfer(UuidGenerator.getUID(), commissionAccount, new Money(1000), new Money(1000), null);


    Long beforeCommission = commissionAccount.balance().getCents();
    Long beforePayable = payable.balance().getCents();

    Money commission = new Money(80);
    MerchantReverseDTO dto = new MerchantReverseDTO(merchantId, commission, Collections.emptyMap());

    Transaction transaction = walletService.reverseCommission(dto);
    List<Transaction> actual = transactionRepository.transactionsOfPid(transaction.getPid());

    assertThat(actual).extracting("srcAmount", "dstAmount", "srcAccountType", "dstAccountType", "srcUserId", "dstUserId").containsExactly(
        tuple(commission.getCents(), commission.getCents(), AccountType.MERCHANT_COMMISSION, AccountType.PAYABLE, commissionAccount.wallet().userId, merchantWallet.userId)
    );

    assertThat(commissionAccount.balance().getCents()).isEqualTo(beforeCommission - commission.getCents());
    assertThat(payable.balance().getCents()).isEqualTo(beforePayable + commission.getCents());
  }

  @Test(expected = DebitNotAllowedException.class)
  public void reverseZeroCommission() {
    Money commission = Money.ZERO_JPY;
    MerchantReverseDTO dto = new MerchantReverseDTO(merchantId, commission, Collections.emptyMap());
    walletService.reverseCommission(dto);
  }

  @Test(expected = DebitNotAllowedException.class)
  public void reverseNegativeCommission() {
    Money commission = new Money(-1);
    MerchantReverseDTO dto = new MerchantReverseDTO(merchantId, commission, Collections.emptyMap());
    walletService.reverseCommission(dto);
  }

  @Test
  public void reverseTax() {
    // prepare
    Account payable = merchantWallet.payable();
    Account source = walletFactory.rebuildExternalWallet().source();
    source.transfer(UuidGenerator.getUID(), payable, new Money(1000), new Money(1000), null);

    Account taxAccount = walletFactory.rebuildSystemWallet().getTax();
    source.transfer(UuidGenerator.getUID(), taxAccount, new Money(1000), new Money(1000), null);

    Long beforeTax = taxAccount.balance().getCents();
    Long beforePayable = payable.balance().getCents();

    Money tax = new Money(80);
    MerchantReverseDTO dto = new MerchantReverseDTO(merchantId, tax, Collections.emptyMap());

    Transaction transaction = walletService.reverseTax(dto);
    List<Transaction> actual = transactionRepository.transactionsOfPid(transaction.getPid());

    assertThat(actual).extracting("srcAmount", "dstAmount", "srcAccountType", "dstAccountType", "srcUserId", "dstUserId").containsExactly(
        tuple(tax.getCents(), tax.getCents(), AccountType.TAX, AccountType.PAYABLE, taxAccount.wallet().userId, merchantWallet.userId)
    );

    assertThat(taxAccount.balance().getCents()).isEqualTo(beforeTax - tax.getCents());
    assertThat(payable.balance().getCents()).isEqualTo(beforePayable + tax.getCents());
  }

  @Test(expected = DebitNotAllowedException.class)
  public void reverseZeroTax() {
    Money commission = Money.ZERO_JPY;
    MerchantReverseDTO dto = new MerchantReverseDTO(merchantId, commission, Collections.emptyMap());
    walletService.reverseTax(dto);
  }

  @Test(expected = DebitNotAllowedException.class)
  public void reverseNegativeTax() {
    Money commission = new Money(-1);
    MerchantReverseDTO dto = new MerchantReverseDTO(merchantId, commission, Collections.emptyMap());
    walletService.reverseTax(dto);
  }

  @Test
  public void expirePrepareToMerchantCampaignTransactionContentsTest() {
    // fund merchant campaign account
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // two expirable cashbacks with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    long depositPID_1 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    long depositPID_2 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 100), metadata);

    List<ExpirableMoney> expirableMoneyList = expirableMoneyService
        .getExpirableMoneyCollection(customerWallet.getUserId(), expiry_date1)
        .filterInByExpiryDate(expiry_date1);
    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    List<TransactionV2DTO>  transactionV2DTOS = walletService.expireCashbackPrepare(request);
    assertThat(transactionV2DTOS.size()).isEqualTo(expirableMoneyList.size());

    for (int index = 0; index < expirableMoneyList.size(); index ++) {
      long pid = Long.parseLong(transactionV2DTOS.get(index).getPid());
      List<Transaction> prepareTxns = transactionRepository.transactionsOfPid(pid);
      List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(expirableMoneyList.get(index).getOrigin().getPid());

      Money moneyToExpire = expirableMoneyList.get(index).getAmount();
      assertThat(prepareTxns).extracting("srcAmount", "dstAmount", "srcAccountType", "dstAccountType", "srcUserId", "dstUserId").containsExactly(
          Tuple.tuple(moneyToExpire.getCents(), moneyToExpire.getCents(), AccountType.CASHBACK_EXPIRABLE, AccountType.EXPIRED, customerWallet.getUserId(), customerWallet.getUserId())
      );
      assertThat(prepareTxns.get(0).getMetadataByKey("merchant_id")).isEqualTo(merchantIdWithOneCampaign);
      assertThat(prepareTxns.get(0).getMetadataByKey("campaign_token")).isEqualTo(campaignToken);
      assertThat(prepareTxns.get(0).getMetadataByKey(MetaKeys.MERCHANT_NAME)).isEqualTo(merchantName);
      assertThat(prepareTxns.get(0).getMetadataByKey(MetaKeys.ORDER_ID)).isEqualTo(orderId);
      assertThat(prepareTxns.get(0).getMetadataByKey(MetaKeys.NOTES)).isEqualTo(merchantCampaignNotes);
      assertThat(derivedTransactions.get(0).getDerivedPid()).isEqualTo(pid);
      assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.EXPIRE);

    }
  }

  @Test
  public void expirePrepareAccountBalanceTest() {
    // fund merchant campaign account
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // account balance before cashback deposit
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);

    // two expirable cashbacks deposit with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE,  new Money(Currency.JPY, 100), metadata);

    // one expirable cashback deposit with expiry_date2
    metadata = new HashMap<>();
    Date expiry_date2 = DateUtils.dateFromDays("7");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date2));
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);

    // account balance after cashback deposit
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(400L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(400);

    // expire/prepare with expiry_date1
    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    walletService.expireCashbackPrepare(request);

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(250L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);

    // expire/prepare with expiry_date2
    request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date2), new HashMap<>());
    walletService.expireCashbackPrepare(request);

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(400);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);
  }


  @Test
  public void expirePrepareExpiryStatusTest() {
    // fund merchant campaign account
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // two expirable cashbacks deposit with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    long depositPID_1 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    long depositPID_2 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 100), metadata);

    // one expirable cashback deposit with expiry_date2
    metadata = new HashMap<>();
    Date expiry_date2 = DateUtils.dateFromDays("7");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date2));
    long depositPID_3 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);

    // expire/prepare with expiry_date1
    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    walletService.expireCashbackPrepare(request);
    CashbackExpiry expiryRecord_1 = cashbackExpiryRepository.cashbackExpiryOfPid(depositPID_1).get(0);
    CashbackExpiry expiryRecord_2 = cashbackExpiryRepository.cashbackExpiryOfPid(depositPID_2).get(0);
    CashbackExpiry expiryRecord_3 = cashbackExpiryRepository.cashbackExpiryOfPid(depositPID_3).get(0);
    assertThat(expiryRecord_1.getStatus()).isEqualTo(CashbackExpiryStatus.EXPIRED);
    assertThat(expiryRecord_2.getStatus()).isEqualTo(CashbackExpiryStatus.EXPIRED);
    assertThat(expiryRecord_3.getStatus()).isEqualTo(CashbackExpiryStatus.COMPLETED);

    // expire/prepare with expiry_date2
    request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date2), new HashMap<>());
    walletService.expireCashbackPrepare(request);
    expiryRecord_3 = cashbackExpiryRepository.cashbackExpiryOfPid(depositPID_3).get(0);
    assertThat(expiryRecord_3.getStatus()).isEqualTo(CashbackExpiryStatus.EXPIRED);
  }

  @Test
  public void expirePartiallyUsedCashbackTest() {
    // fund merchant campaign account
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // four expirable cashbacks deposit with expiry_date1: 1000
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    long pid1 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 200), metadata);
    long pid2 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE,  new Money(Currency.JPY, 300), metadata);
    long pid3 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE,  new Money(Currency.JPY, 400), metadata);
    long pid4 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE,  new Money(Currency.JPY, 100), metadata);

    // one expirable cashback deposit with expiry_date2: 150
    metadata = new HashMap<>();
    Date expiry_date2 = DateUtils.dateFromDays("7");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date2));
    long pid5 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 75), metadata);
    long pid6 = performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 75), metadata);

    // account balance after cashback deposit
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(1150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(1150L);

    // use 350
    long txnId1 = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 350), null));
    walletService.capture(new CustomerCaptureDTO(Arrays.asList(txnId1), null));

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(800L);
    assertThat(cwallet.prepaid().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.cashback().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(800L);

    // expire/prepare with expiry_date1: will expire 650
    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    List<TransactionV2DTO> transactionV2DTOs = walletService.expireCashbackPrepare(request);

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(650L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);

    assertThat(transactionV2DTOs.size()).isEqualTo(3);
    Transaction t1 = transactionRepository.transactionsOfPid(Long.parseLong(transactionV2DTOs.get(0).getPid())).get(0);
    assertThat(t1.getDstAmount()).isEqualTo(150); // should be 150
    assertThat(t1.getMetadataByKey("original_pid")).isEqualTo(String.format("%d",pid2));

    Transaction t2 = transactionRepository.transactionsOfPid(Long.parseLong(transactionV2DTOs.get(1).getPid())).get(0);
    assertThat(t2.getDstAmount()).isEqualTo(400); // should be 400
    assertThat(t2.getMetadataByKey("original_pid")).isEqualTo(String.format("%d",pid3));

    Transaction t3 = transactionRepository.transactionsOfPid(Long.parseLong(transactionV2DTOs.get(2).getPid())).get(0);
    assertThat(t3.getDstAmount()).isEqualTo(100); // should be 100
    assertThat(t3.getMetadataByKey("original_pid")).isEqualTo(String.format("%d",pid4));

    assertThat(cashbackExpiryRepository.cashbackExpiryOfPid(pid1).get(0).getStatus()).isEqualTo(CashbackExpiryStatus.COMPLETED); // completely used
    assertThat(cashbackExpiryRepository.cashbackExpiryOfPid(pid2).get(0).getStatus()).isEqualTo(CashbackExpiryStatus.EXPIRED); // partially used
    assertThat(cashbackExpiryRepository.cashbackExpiryOfPid(pid3).get(0).getStatus()).isEqualTo(CashbackExpiryStatus.EXPIRED); // completely expired
    assertThat(cashbackExpiryRepository.cashbackExpiryOfPid(pid4).get(0).getStatus()).isEqualTo(CashbackExpiryStatus.EXPIRED); // completely exired
  }

  @Test
  public void expirePrepareTwiceTest() {
    // fund merchant campaign account
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // two expirable cashbacks with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 100), metadata);

    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    List<TransactionV2DTO>  transactionV2DTOS = walletService.expireCashbackPrepare(request);

    assertThat(transactionV2DTOS.size()).isEqualTo(2);
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(250L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);

    // no change
    transactionV2DTOS = walletService.expireCashbackPrepare(request);
    assertThat(transactionV2DTOS.size()).isEqualTo(0);
    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(250L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);
  }

  @Test(expected = ExpiredCashbackDestinationTypeNotAllowedException.class)
  public void expirePrepareWithNotExistExpiredDstTypeInCashbackExpiryMetadata() {
    // Setup
    Date expiryDate = DateUtils.dateFromDays("4");
    Map<String, String> invalidMeta = new HashMap<>();
    cashbackExpiryRepository.add(new CashbackExpiry(1L, 1L, new Money(100), customerWallet.getUserId(), expiryDate, CashbackExpiryStatus.COMPLETED, invalidMeta));
    funding(AccountType.CASHBACK_EXPIRABLE, 100);
    // Main
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>());
    walletService.expireCashbackPrepare(prepare);
  }

  @Test(expected = InvalidMetadataException.class)
  public void expirePrepareToMerchantCampaignWithBlankMerchantIdTest() {
    // Setup
    Date expiryDate = DateUtils.dateFromDays("4");
    Map<String, String> invalidMeta = new HashMap<>();
    invalidMeta.put(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE, ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());
    invalidMeta.put(MetaKeys.MERCHANT_ID, "");
    invalidMeta.put(MetaKeys.CAMPAIGN_TOKEN_SNAKE, "not_blank");
    cashbackExpiryRepository.add(new CashbackExpiry(1L, 1L, new Money(100), customerWallet.getUserId(), expiryDate, CashbackExpiryStatus.COMPLETED, invalidMeta));
    funding(AccountType.CASHBACK_EXPIRABLE, 100);
    // Main
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>());
    walletService.expireCashbackPrepare(prepare);
  }

  @Test(expected = InvalidMetadataException.class)
  public void expirePrepareToMerchantCampaignWithBlankCampaignTokenTest() {
    // Setup
    Date expiryDate = DateUtils.dateFromDays("4");
    Map<String, String> invalidMeta = new HashMap<>();
    invalidMeta.put(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE, ExpiredCashbackDestinationType.MERCHANT_CAMPAIGN.name());
    invalidMeta.put(MetaKeys.MERCHANT_ID, "not_blank");
    invalidMeta.put(MetaKeys.CAMPAIGN_TOKEN_SNAKE, "");
    cashbackExpiryRepository.add(new CashbackExpiry(1L, 1L, new Money(100), customerWallet.getUserId(), expiryDate, CashbackExpiryStatus.COMPLETED, invalidMeta));
    funding(AccountType.CASHBACK_EXPIRABLE, 100);
    // Main
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>());
    walletService.expireCashbackPrepare(prepare);
  }

  @Test(expected = InvalidMetadataException.class)
  public void expirePrepareToSystemCampaignWithBlankCampaignTokenTest() {
    // Setup
    Date expiryDate = DateUtils.dateFromDays("4");
    Map<String, String> invalidMeta = new HashMap<>();
    invalidMeta.put(MetaKeys.EXPIRED_CASHBACK_DESTINATION_TYPE, ExpiredCashbackDestinationType.SYSTEM_CAMPAIGN.name());
    invalidMeta.put(MetaKeys.CAMPAIGN_TOKEN_SNAKE, "");
    cashbackExpiryRepository.add(new CashbackExpiry(1L, 1L, new Money(100), customerWallet.getUserId(), expiryDate, CashbackExpiryStatus.COMPLETED, invalidMeta));
    funding(AccountType.CASHBACK_EXPIRABLE, 100);
    // Main
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiryDate), new HashMap<>());
    walletService.expireCashbackPrepare(prepare);
  }

  @Test
  public void expireConfirmToMerchantCampaignTest() {

    // fund merchant campaign account with 10000
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // two expirable cashbacks deposit with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE,  new Money(Currency.JPY, 100), metadata);

    // one expirable cashback deposit with expiry_date2
    metadata = new HashMap<>();
    Date expiry_date2 = DateUtils.dateFromDays("7");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date2));
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);

    // account balance after cashback deposit
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    MerchantWallet mwallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantIdWithOneCampaign);
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(400L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(400L);
    assertThat(mwallet.campaign(campaignToken).get().balance().getCents()).isEqualTo(9600);

    // expire/prepare with expiry_date1
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    List<TransactionV2DTO> preparePids = walletService.expireCashbackPrepare(prepare);

    // check metadata of prepare txn for passbook
    assertThat(preparePids.size()).isEqualTo(2);
    Transaction prepareTxn1 = transactionRepository.transactionsOfPid(Long.parseLong(preparePids.get(0).getPid())).get(0);
    Transaction prepareTxn2 = transactionRepository.transactionsOfPid(Long.parseLong(preparePids.get(1).getPid())).get(0);
    assertThat(prepareTxn1.getMetadataByKey(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(prepareTxn2.getMetadataByKey(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(prepareTxn1.getMetadataByKey(MetaKeys.MERCHANT_NAME)).isEqualTo(merchantName);
    assertThat(prepareTxn2.getMetadataByKey(MetaKeys.MERCHANT_NAME)).isEqualTo(merchantName);
    assertThat(prepareTxn1.getMetadataByKey(MetaKeys.NOTES)).isEqualTo(merchantCampaignNotes);
    assertThat(prepareTxn2.getMetadataByKey(MetaKeys.NOTES)).isEqualTo(merchantCampaignNotes);

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(250L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);

    // expire/confirm with first pid
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(0).getPid()), new HashMap<>());
    TransactionV2DTO confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(0).getPid()).isEqualTo(confirmPid.getPid());

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    mwallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantIdWithOneCampaign);
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(100L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);
    assertThat(mwallet.campaign(campaignToken).get().balance().getCents()).isEqualTo(9750);

    // expire/confirm with second pid
    confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(1).getPid()), new HashMap<>());
    confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(1).getPid()).isEqualTo(confirmPid.getPid());

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    mwallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantIdWithOneCampaign);
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);
    assertThat(mwallet.campaign(campaignToken).get().balance().getCents()).isEqualTo(9850);

  }

  @Test
  public void expireConfirmToSystemCampaignTest() {

    // fund system campaign account with 10000
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneSystemCampaign(campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // two expirable cashbacks deposit with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    performSystemCashbackDeposit(campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);
    performSystemCashbackDeposit(campaignToken, AccountType.CASHBACK_EXPIRABLE,  new Money(Currency.JPY, 100), metadata);

    // one expirable cashback deposit with expiry_date2
    metadata = new HashMap<>();
    Date expiry_date2 = DateUtils.dateFromDays("7");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date2));
    performSystemCashbackDeposit(campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);

    // account balance after cashback deposit
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    SystemWallet swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(400L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(400L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9600);

    // expire/prepare with expiry_date1
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    List<TransactionV2DTO> preparePids = walletService.expireCashbackPrepare(prepare);

    // check metadata of prepare txn for passbook
    assertThat(preparePids.size()).isEqualTo(2);
    Transaction prepareTxn1 = transactionRepository.transactionsOfPid(Long.parseLong(preparePids.get(0).getPid())).get(0);
    Transaction prepareTxn2 = transactionRepository.transactionsOfPid(Long.parseLong(preparePids.get(1).getPid())).get(0);
    assertThat(prepareTxn1.getMetadataByKey(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(prepareTxn2.getMetadataByKey(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(prepareTxn1.getMetadataByKey(MetaKeys.MERCHANT_NAME)).isNull();
    assertThat(prepareTxn2.getMetadataByKey(MetaKeys.MERCHANT_NAME)).isNull();

    assertThat(prepareTxn1.getMetadataByKey(MetaKeys.NOTES)).isEqualTo(systemCampaignNotes);
    assertThat(prepareTxn2.getMetadataByKey(MetaKeys.NOTES)).isEqualTo(systemCampaignNotes);

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(250L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);

    // expire/confirm with first pid
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(0).getPid()), new HashMap<>());
    TransactionV2DTO confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(0).getPid()).isEqualTo(confirmPid.getPid());

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(100L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9750);

    // expire/confirm with second pid
    confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(1).getPid()), new HashMap<>());
    confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(1).getPid()).isEqualTo(confirmPid.getPid());

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9850);

  }

  @Test
  public void expireConfirmToSystemExpiredTest() {

    // fund system campaign account with 10000
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneSystemCampaign(campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // expirable cashbacks deposit
    Date expiry_date = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date));
    performSystemCashbackDeposit(campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);

    // account balance after cashback deposit
    CustomerWallet cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    SystemWallet swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(150L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9850);

    // payment and refund
    long pid = walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 150), new HashMap<>()));
    walletService.capture(new CustomerCaptureDTO(Collections.singletonList(pid), new HashMap<>()));
    Map<String, String> refundMeta = new HashMap<>();
    refundMeta.put(MetaKeys.ORDER_ID, orderId);
    walletService.refund(new MerchantRefundDTO(merchantWallet.getUserId(), new Money(Currency.JPY, 150), new Money(0), pid, refundMeta));
    Date refundedExpiryDate = DateUtils.getStartOfJstDay(DateUtils.dateFromDays("60"));

    // expire/prepare
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(refundedExpiryDate), new HashMap<>());
    List<TransactionV2DTO> preparePids = walletService.expireCashbackPrepare(prepare);

    // check metadata of prepare txn for passbook
    assertThat(preparePids.size()).isEqualTo(1);
    Transaction prepareTxn = transactionRepository.transactionsOfPid(Long.parseLong(preparePids.get(0).getPid())).get(0);
    assertThat(prepareTxn.getMetadataByKey(MetaKeys.ORDER_ID)).isEqualTo(orderId);
    assertThat(prepareTxn.getMetadataByKey(MetaKeys.MERCHANT_NAME)).isNull();
    assertThat(prepareTxn.getMetadataByKey(MetaKeys.NOTES)).isNull();

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(150L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);
    long beforeConfirmBalanaceOfSystemExpired = swallet.systemExpired().balance().getCents();

    // expire/confirm
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(0).getPid()), new HashMap<>());
    TransactionV2DTO confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(0).getPid()).isEqualTo(confirmPid.getPid());

    cwallet = walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    swallet = walletFactory.rebuildSystemWallet();
    assertThat(cwallet.cashbackExpirable().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.expired().balance().getCents()).isEqualTo(0L);
    assertThat(cwallet.getTotalBalance()).isEqualTo(0L);
    assertThat(swallet.campaign(campaignToken).balance().getCents()).isEqualTo(9850L);
    assertThat(swallet.systemExpired().balance().getCents()).isEqualTo(beforeConfirmBalanaceOfSystemExpired + 150L);
  }

  @Test (expected = MacroTxnStateMismatchException.class)
  public void expireConfirmTwiceTest() {

    // fund merchant campaign account with 10000
    String campaignToken = String.valueOf(UuidGenerator.getUID());
    createAndFundOneMerchantCampaign(merchantIdWithOneCampaign, campaignToken);
    Map<String, String> metadata = new HashMap<>();

    // one expirable cashbacks deposit with expiry_date1
    Date expiry_date1 = DateUtils.dateFromDays("4");
    metadata.put("expiry_date", DateUtils.dateToString(expiry_date1));
    performMerchantCashbackDeposit(merchantIdWithOneCampaign, campaignToken, AccountType.CASHBACK_EXPIRABLE, new Money(Currency.JPY, 150), metadata);

    // expire/prepare with expiry_date1
    PrepareCustomerCashbackExpiryDTO prepare = new PrepareCustomerCashbackExpiryDTO(customerWallet.getUserId(), DateUtils.dateToString(expiry_date1), new HashMap<>());
    List<TransactionV2DTO> preparePids = walletService.expireCashbackPrepare(prepare);

    // expire/confirm with first pid
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(Long.parseLong(preparePids.get(0).getPid()), new HashMap<>());
    TransactionV2DTO confirmPid = walletService.processCashbackExpireConfirmRequest(confirm);
    assertThat(preparePids.get(0).getPid()).isEqualTo(confirmPid.getPid());

    // expire/confirm with same pid again
    walletService.processCashbackExpireConfirmRequest(confirm);
  }

  @Test (expected = NoSuchMacroTxnException.class)
  public void orphanExpireConfirmTest() {
    ConfirmOrCancelDTO confirm = new ConfirmOrCancelDTO(UuidGenerator.getUID(), new HashMap<>());
    walletService.processCashbackExpireConfirmRequest(confirm);
  }

  @Test
  public void createEmoneyWalletShouldSucceed() {
    CreateCustomerEmoneyAccountDTO dto = new CreateCustomerEmoneyAccountDTO(nonKycCustomerId, new HashMap<>());
    Account emoneyAccount = walletService.createEmoneyAccount(dto);
    assertThat(emoneyAccount.getId()).isNotNull();
    assertThat(emoneyAccount.getType()).isEqualTo(AccountType.EMONEY);
    assertThat(emoneyAccount.isVisible()).isTrue();
    assertThat(emoneyAccount.balance().getCents()).isEqualTo(0L);
  }

  @Test
  public void createEmoneyWalletTwiceShouldReturnSameResult() {
    CreateCustomerEmoneyAccountDTO dto = new CreateCustomerEmoneyAccountDTO(kycCustomerWallet.userId, new HashMap<>());
    Account second = walletService.createEmoneyAccount(dto);
    assertThat(kycCustomerWallet.emoney().get()).isEqualToComparingFieldByField(second);

  }

  @Test(expected = WalletNotFoundException.class)
  public void createEmoneyWalletShouldFailWithNonExistingUserId() {
    String userId = "NON_EXISTING_USERID";
    CreateCustomerEmoneyAccountDTO dto = new CreateCustomerEmoneyAccountDTO(userId, new HashMap<>());
    walletService.createEmoneyAccount(dto);
  }

  private void funding(Wallet dst, AccountType dstAccountType, long fundingAmount) {
    Account source = walletFactory.rebuildExternalWallet().source();
    Account dstAccount = dst.findAccountByType(dstAccountType).get();

    source.transfer(
        UuidGenerator.getUID(),
        dstAccount,
        new Money(Currency.JPY, fundingAmount),
        new Money(Currency.JPY, fundingAmount),
        null);
  }

  private void funding(AccountType dstAccountType, long fundingAmount) {
    funding(customerWallet, dstAccountType, fundingAmount);
  }

  private void fundingKycCustomerWallet() {
    Account source = walletFactory.rebuildExternalWallet().source();

    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet.cashbackExpirable(),
        new Money(Currency.JPY, 200L),
        new Money(Currency.JPY, 200L),
        null);
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
        kycCustomerWallet1.cashbackExpirable(),
        new Money(Currency.JPY, 200L),
        new Money(Currency.JPY, 200L),
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

  private void fundingKycCustomerWalletForP2p() {
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
        new Money(Currency.JPY, 100000L),
        new Money(Currency.JPY, 100000L),
        null);
    source.transfer(
        UuidGenerator.getUID(),
        kycCustomerWallet.emoney().get(),
        new Money(Currency.JPY, 500L),
        new Money(Currency.JPY, 500L),
        null);
  }

  private MerchantCampaignCashbackDepositDTO getMerchantCashbackDepositRequest(String merchantId, String campaignToken, AccountType type, Money cashback, Map<String, String> metadata) {
    createAndFundOneMerchantCampaign(merchantId, campaignToken);
    updateMerhandDepositMetadata(metadata);
    MerchantCampaignCashbackDepositDTO request = new MerchantCampaignCashbackDepositDTO();
    request.merchantWalletOwnerId = merchantId;
    request.customer_wallet_owner_id = customerWallet.getUserId();
    request.campaign_token = campaignToken;
    request.amount =  cashback;
    request.setAccountType(type.name());
    request.metadata = metadata;

    return request;
  }

  private CustomerCashbackV3DTO getSystemCashbackDepositRequest(String campaignToken, AccountType type, Money cashback, Map<String, String> metadata) {
    createAndFundOneSystemCampaign(campaignToken);
    updateSystemDepositMetadata(metadata);
    CustomerCashbackV3DTO request = new CustomerCashbackV3DTO();
    request.customer_wallet_owner_id = customerWallet.getUserId();
    request.campaign_token = campaignToken;
    request.amount =  cashback;
    request.setAccountType(type.name());
    request.metadata = metadata;

    return request;
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
    metadata.put(MetaKeys.MERCHANT_NAME, merchantName);
    metadata.put(MetaKeys.ORDER_ID, orderId);
    metadata.put(MetaKeys.NOTES, merchantCampaignNotes);

    return metadata;
  }

  private Map<String, String> updateSystemDepositMetadata(Map<String, String> metadata) {

    metadata.put("accepted_at", DateUtils.getNow());
    metadata.put("processed_at", DateUtils.getNow());
    metadata.put(MetaKeys.ORDER_ID, orderId);
    metadata.put(MetaKeys.NOTES, systemCampaignNotes);

    return metadata;
  }
}
