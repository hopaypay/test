package com.paytm.paylite.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.paytm.paylite.wallet.application.CustomerWalletApplicationService;
import com.paytm.paylite.wallet.controller.dto.request.BatchRequestElement;
import com.paytm.paylite.wallet.controller.dto.request.CampaignDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.CampaignTopupDTO;
import com.paytm.paylite.wallet.controller.dto.request.CampaignTopupReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.ConfirmOrCancelDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateCustomerEmoneyAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateCustomerWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.CreateMerchantWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerAuthorizeDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCaptureDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerCashbackV3DTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerGiftcardTopupPrepare;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PConfirmDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerP2PPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerPayoutPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerPrepareDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerReverseDTO;
import com.paytm.paylite.wallet.controller.dto.request.CustomerTopupPrepare;
import com.paytm.paylite.wallet.controller.dto.request.DisableCustomerWalletDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantCampaignCashbackDepositDTO;
import com.paytm.paylite.wallet.controller.dto.request.MerchantCampaignTopupDTO;
import com.paytm.paylite.wallet.controller.dto.request.PrepareCustomerCashbackExpiryDTO;
import com.paytm.paylite.wallet.controller.dto.request.RevertDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.CreateCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.request.admin.TopupCampaignAccountDTO;
import com.paytm.paylite.wallet.controller.dto.response.AccountDTO;
import com.paytm.paylite.wallet.controller.dto.response.TransactionV2DTO;
import com.paytm.paylite.wallet.controller.exception.AccountNotFoundException;
import com.paytm.paylite.wallet.controller.exception.IdempotentException;
import com.paytm.paylite.wallet.domain.wallet.CustomerWallet;
import com.paytm.paylite.wallet.domain.wallet.ExternalWallet;
import com.paytm.paylite.wallet.domain.wallet.MerchantWallet;
import com.paytm.paylite.wallet.domain.wallet.WalletFactory;
import com.paytm.paylite.wallet.domain.wallet.WalletService;
import com.paytm.paylite.wallet.domain.wallet.account.Account;
import com.paytm.paylite.wallet.domain.wallet.account.AccountType;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransaction;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransactionRepository;
import com.paytm.paylite.wallet.domain.wallet.account.DerivedTransactionType;
import com.paytm.paylite.wallet.domain.wallet.account.MacroTransaction;
import com.paytm.paylite.wallet.domain.wallet.account.Transaction;
import com.paytm.paylite.wallet.domain.wallet.account.TransactionRepository;
import com.paytm.paylite.wallet.domain.wallet.account.monetary.Currency;
import com.paytm.paylite.wallet.domain.wallet.account.monetary.Money;
import com.paytm.paylite.wallet.infrastructure.idempotency.IdempotencyService;
import com.paytm.paylite.wallet.infrastructure.uidgenerator.UuidGenerator;
import com.paytm.paylite.wallet.infrastructure.utils.DateUtils;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@WithMockUser(roles={"WALLET::PAYMENT", "WALLET::CLM", "SERVICE_USER", "WALLET::BO_MANAGER", "WALLET::USER", "WALLET::KYC"})
@TestPropertySource(properties = {"wallet.daily.max=100","wallet.monthly.max=200", "wallet.feature.create.emoney.account.enabled=true"})
public class CustomerWalletControllerTest {

  @Autowired private WebApplicationContext wac;
  @Autowired private WalletService walletService;
  @Autowired private WalletFactory walletFactory;
  @Autowired private TransactionRepository transactionRepository;
  @Autowired private DerivedTransactionRepository derivedTransactionRepository;
  @SpyBean private CustomerWalletApplicationService walletApplicationService;

  @Autowired
  private IdempotencyService idempotencyService;
  private MockMvc mockMvc;
  private String customerId;
  private String merchantId;
  private String kycCustomerWalletId;
  private CustomerWallet customerWallet;
  private CustomerWallet receiverWallet;
  private CustomerWallet kycCustomerWallet;
  private MerchantWallet merchantWallet;
  private MerchantWallet merchantWalletWithNonNumericId;
  private Money amount = new Money(Currency.JPY, 100);
  private HttpHeaders headers = new HttpHeaders();
  private String campaignToken = "campaign1";
  private String idemKey = "121421aasdf3";
  private String budgetToken = "budget1";

  @Before
  public void setup() {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
    mockMvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .apply(springSecurity())
        .build();
    headers.add("Idem-Key", idemKey);
    customerId = String.valueOf(UuidGenerator.getUID());
    merchantId = String.valueOf(UuidGenerator.getUID());
    kycCustomerWalletId = String.valueOf(UuidGenerator.getUID());
    Account source = walletFactory.rebuildExternalWallet().source();
    Account budget = walletFactory.rebuildSystemWallet().budget();
    source.transfer(
        UuidGenerator.getUID(),
        budget,
        new Money(Currency.JPY, 10000),
        new Money(Currency.JPY, 10000),
        null);
    customerWallet =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(customerId)));
    kycCustomerWallet =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(kycCustomerWalletId));
    kycCustomerWallet.createAccount(new Account(kycCustomerWallet, AccountType.EMONEY, Currency.JPY, true, new HashMap<>()));

    merchantWallet =
        walletService.createMerchantWallet(new CreateMerchantWalletDTO(merchantId));
    merchantWalletWithNonNumericId =
        walletService.createMerchantWallet(new CreateMerchantWalletDTO(String.valueOf( UUID.randomUUID())));
    receiverWallet =
        (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    walletService.createCampaignAccount(new CreateCampaignAccountDTO(campaignToken, null));
    walletService.topupCampaignAccount(new TopupCampaignAccountDTO(campaignToken, amount));
  }

  @Test
  public void createCustomerWallet() throws Exception {
    CreateCustomerWalletDTO createWalletDTO = new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID()));

    String result =
        mockMvc
            .perform(
                post("/v1/customer/wallets")
                    .headers(headers)
                    .content(JSONObject.toJSONString(createWalletDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.wallet_uuid").isNotEmpty())
            .andExpect(jsonPath("$.wallet_type").value("CUSTOMER"))
            .andExpect(jsonPath("$.accounts").isArray())
            .andExpect(jsonPath("$.accounts.length()").value(10))
            .andExpect(jsonPath("$.accounts[0].type").value("CASHBACK"))
            .andExpect(jsonPath("$.accounts[0].balance").value(0))
            .andExpect(jsonPath("$.accounts[1].type").value("PREPAID"))
            .andExpect(jsonPath("$.accounts[1].visible").value(true))
            .andExpect(jsonPath("$.accounts[1].balance").value(0))
            .andExpect(jsonPath("$.accounts[1].expired_at").isNotEmpty())
            .andExpect(jsonPath("$.accounts[2].type").value("PAYMENT"))
            .andExpect(jsonPath("$.accounts[2].visible").value(false))
            .andExpect(jsonPath("$.accounts[2].balance").value(0))
            .andExpect(jsonPath("$.accounts[3].type").value("PAYOUT"))
            .andExpect(jsonPath("$.accounts[3].visible").value(false))
            .andExpect(jsonPath("$.accounts[3].balance").value(0))
            .andExpect(jsonPath("$.accounts[4].type").value("INCOMING"))
            .andExpect(jsonPath("$.accounts[4].visible").value(false))
            .andExpect(jsonPath("$.accounts[4].balance").value(0))
            .andExpect(jsonPath("$.accounts[5].type").value("CASHBACK_PENDING"))
            .andExpect(jsonPath("$.accounts[5].visible").value(false))
            .andExpect(jsonPath("$.accounts[5].balance").value(0))
            .andExpect(jsonPath("$.accounts[6].type").value("P2P_PENDING"))
            .andExpect(jsonPath("$.accounts[6].visible").value(false))
            .andExpect(jsonPath("$.accounts[6].balance").value(0))
            .andExpect(jsonPath("$.accounts[7].type").value("CAMPAIGN"))
            .andExpect(jsonPath("$.accounts[7].visible").value(false))
            .andExpect(jsonPath("$.accounts[7].balance").value(0))
            .andExpect(jsonPath("$.accounts[8].type").value("CASHBACK_EXPIRABLE"))
            .andExpect(jsonPath("$.accounts[8].visible").value(true))
            .andExpect(jsonPath("$.accounts[8].balance").value(0))
            .andExpect(jsonPath("$.accounts[9].type").value("EXPIRED"))
            .andExpect(jsonPath("$.accounts[9].visible").value(false))
            .andExpect(jsonPath("$.accounts[9].balance").value(0))
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void createCustomerWalletIdempotentException() throws Exception {
    CreateCustomerWalletDTO createWalletDTO = new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID()));
    Mockito.doThrow(new IdempotentException("fake")).when(walletApplicationService).createCustomerWallet(Matchers.any(), Matchers.any());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/wallets")
                    .headers(headers)
                    .content(JSONObject.toJSONString(createWalletDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.resultInfo.resultCode").value("KEEP_LAST_FAILURE"))
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void createCustomerWalletSuccessIdempotently() throws Exception {
    CreateCustomerWalletDTO createWalletDTO = new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID()));

    String result =
        mockMvc
            .perform(
                post("/v1/customer/wallets")
                    .headers(headers)
                    .content(JSONObject.toJSONString(createWalletDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String wallet_uuid = JsonPath.read(result, "$.wallet_uuid");

    createWalletDTO.setCustomer_id(createWalletDTO.getCustomer_id()+ "a");
    result =
        mockMvc
            .perform(
                post("/v1/customer/wallets")
                    .headers(headers)
                    .content(JSONObject.toJSONString(createWalletDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.wallet_uuid").value(wallet_uuid))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void createCustomerWalletFailureIdempotently() throws Exception {
    CreateCustomerWalletDTO createWalletDTO = new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID()));

    Mockito.doThrow(new AccountNotFoundException("fake")).when(walletApplicationService).createCustomerWallet(Matchers.any(), Matchers.any());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/wallets")
                    .headers(headers)
                    .content(JSONObject.toJSONString(createWalletDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    createWalletDTO.setCustomer_id(createWalletDTO.getCustomer_id()+ "a");
    result =
        mockMvc
            .perform(
                post("/v1/customer/wallets")
                    .headers(headers)
                    .content(JSONObject.toJSONString(createWalletDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
    String resultCode = JsonPath.read(result, "$.resultInfo.resultCode");
    String resultMsg = JsonPath.read(result, "$.resultInfo.resultMsg");
    assertThat(resultCode).isEqualTo("ACCOUNT_NOT_FOUND");
    assertThat(resultMsg).isEqualTo("fake");

  }

  @Test
  public void disableCustomerWallet() throws Exception {
    assertThat(customerWallet.getMetadataByKey("disabled")).isNull();
    assertThat(customerWallet.getMetadataByKey("disabled_at")).isNull();

    DisableCustomerWalletDTO request = new DisableCustomerWalletDTO(true);

    mockMvc
        .perform(
            put("/v1/customer/wallets/" + customerWallet.getUserId())
                .headers(headers)
                .content(JSONObject.toJSONString(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    assertThat(customerWallet.getMetadataByKey("disabled")).isEqualTo("true");
    assertThat(customerWallet.getMetadataByKey("disabled_at")).isNotNull();

    String disabledAtShouldNotBeUpdated = customerWallet.getMetadataByKey("disabled_at");

    mockMvc
        .perform(
            put("/v1/customer/wallets/" + customerWallet.getUserId())
                .headers(headers)
                .content(JSONObject.toJSONString(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    assertThat(customerWallet.getMetadataByKey("disabled")).isEqualTo("true");
    assertThat(customerWallet.getMetadataByKey("disabled_at")).isEqualTo(disabledAtShouldNotBeUpdated);
  }

  @Test
  public void disableCustomerWalletException() throws Exception {
    assertThat(customerWallet.getMetadataByKey("disabled")).isNull();
    assertThat(customerWallet.getMetadataByKey("disabled_at")).isNull();

    DisableCustomerWalletDTO request = new DisableCustomerWalletDTO(false);

    String result =
        mockMvc
            .perform(
                put("/v1/customer/wallets/" + customerWallet.getUserId())
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
    String resultCode = JsonPath.read(result, "$.resultInfo.resultCode");
    String resultMsg = JsonPath.read(result, "$.resultInfo.resultMsg");

    assertThat(resultCode).isEqualTo("ILLEGAL_ARGUMENT");
    assertThat(resultMsg).isEqualTo("disabled value must be true");
    assertThat(customerWallet.getMetadataByKey("disabled")).isNull();
    assertThat(customerWallet.getMetadataByKey("disabled_at")).isNull();
  }

  @Test
  public void topupPrepare() throws Exception {
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    long beforeE = externalWallet.source().getUpdatedAt().getTime();

    long amount = 100;
    Map<String, String> metadata = new HashMap<>();
    metadata.put("payment_method_type", "BANK");
    CustomerTopupPrepare request =
        new CustomerTopupPrepare(
            String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, amount), metadata);


    String requestStr =  JSONObject.toJSONString(request,  SerializerFeature.WriteMapNullValue);

    String result =
        mockMvc
            .perform(
                post("/v1/customer/topup/prepare")
                    .headers(headers)
                    .content(requestStr)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String pid = JsonPath.read(result, "$.pid");

    CustomerWallet dbCustomerWallet =
        walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(amount);

    assertThat(externalWallet.source().getUpdatedAt().getTime()).isEqualTo(beforeE);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.valueOf(pid));
    assertThat(transactions.size()).isEqualTo(1);
    assertThat(transactions.get(0).getDstBalance()).isEqualTo(0);
    assertThat(transactions.get(0).getSrcBalance()).isEqualTo(0);
  }

  @Test
  public void topupConfirm() throws Exception {

    long amount = 100;
    Map<String, String> metadata = new HashMap<>();
    metadata.put("payment_method_type", "BANK");
    CustomerTopupPrepare request =
        new CustomerTopupPrepare(
            String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, amount), metadata);
    long txnId = walletService.customerTopupPrepare(request);

    mockMvc
        .perform(
            post("/v1/customer/topup/confirm")
                .headers(headers)
                .content(JSONObject.toJSONString(new ConfirmOrCancelDTO(txnId, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    CustomerWallet dbCustomerWallet =
        walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(amount);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getMetadataByKey("request_path")).isEqualTo("/v1/customer/topup/confirm");
  }
  @Test
  public void topupConfirmWithSameIdemKey() throws Exception {

    long amount = 100;
    Map<String, String> metadata = new HashMap<>();
    metadata.put("payment_method_type", "BANK");
    CustomerTopupPrepare request =
        new CustomerTopupPrepare(
            String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, amount), metadata);
    long txnId = walletService.customerTopupPrepare(request);

    mockMvc
        .perform(
            post("/v1/customer/topup/confirm")
                .headers(headers)
                .content(JSONObject.toJSONString(new ConfirmOrCancelDTO(txnId, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/v1/customer/topup/confirm")
                .headers(headers)
                .content(JSONObject.toJSONString(new ConfirmOrCancelDTO(txnId, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
  }
  @Test
  public void topupCancel() throws Exception {

    Map<String, String> metadata = new HashMap<>();
    metadata.put("payment_method_type", "BANK");
    CustomerTopupPrepare request =
        new CustomerTopupPrepare(
            String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, 100), metadata);
    long txnId = walletService.customerTopupPrepare(request);

    mockMvc
        .perform(
            post("/v1/customer/topup/cancel")
                .headers(headers)
                .content(JSONObject.toJSONString(new ConfirmOrCancelDTO(txnId, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    CustomerWallet dbCustomerWallet =
        walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    assertThat(transactions.size()).isEqualTo(2);
  }

  @Test
  public void topupRevert() throws Exception {

    long amount = 100;
    Map<String, String> metadata = new HashMap<>();
    metadata.put("payment_method_type", "BANK");
    CustomerTopupPrepare prepare =
        new CustomerTopupPrepare(
            String.valueOf(customerWallet.getUserId()), new Money(Currency.JPY, amount), metadata);
    long txnId = walletService.customerTopupPrepare(prepare);
    walletService.customerTopupConfirm(new ConfirmOrCancelDTO(txnId, null));

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");

    RevertDTO request = new RevertDTO(txnId, meta);

    String result =
        mockMvc
            .perform(
                post("/v1/customer/topup/revert")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void giftcardTopupPrepare() throws Exception {
    SerializeConfig.getGlobalInstance()
            .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    ExternalWallet externalWallet = walletFactory.rebuildExternalWallet();
    long beforeE = externalWallet.source().getUpdatedAt().getTime();

    long amount = 100;
    CustomerGiftcardTopupPrepare request =
            new CustomerGiftcardTopupPrepare(
                    String.valueOf(customerWallet.getUserId()),
                    "CASHBACK",
                    new Money(Currency.JPY, amount),
                    null);

    String requestStr = JSONObject.toJSONString(request,  SerializerFeature.WriteMapNullValue);

    String result =
            mockMvc
                    .perform(
                            post("/v1/customer/topup/giftcard/prepare")
                                    .headers(headers)
                                    .content(requestStr)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pid").isNotEmpty())
                    .andExpect(jsonPath("$.created_at").isNotEmpty())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

    String pid = JsonPath.read(result, "$.pid");

    CustomerWallet dbCustomerWallet =
            walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(amount);

    assertThat(externalWallet.source().getUpdatedAt().getTime()).isEqualTo(beforeE);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(Long.valueOf(pid));
    assertThat(transactions.size()).isEqualTo(1);
    assertThat(transactions.get(0).getDstBalance()).isEqualTo(0);
    assertThat(transactions.get(0).getSrcBalance()).isEqualTo(0);
  }

  @Test
  public void giftcardTopupConfirm() throws Exception {
    SerializeConfig.getGlobalInstance()
            .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    long amount = 100;
    CustomerGiftcardTopupPrepare request =
            new CustomerGiftcardTopupPrepare(
                    String.valueOf(customerWallet.getUserId()),
                    "CASHBACK",
                    new Money(Currency.JPY, amount),
                    null);
    long txnId = walletService.customerGiftcardTopupPrepare(request).getPid();

    mockMvc
            .perform(
                    post("/v1/customer/topup/giftcard/confirm")
                            .headers(headers)
                            .content(JSONObject.toJSONString(new ConfirmOrCancelDTO(txnId, null)))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

    CustomerWallet dbCustomerWallet =
            walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(amount);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getMetadataByKey("request_path")).isEqualTo("/v1/customer/topup/giftcard/confirm");
  }

  @Test
  public void giftcardTopupCancel() throws Exception {
    SerializeConfig.getGlobalInstance()
            .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    long amount = 100;
    CustomerGiftcardTopupPrepare request =
            new CustomerGiftcardTopupPrepare(
                    String.valueOf(customerWallet.getUserId()),
                    "CASHBACK",
                    new Money(Currency.JPY, amount),
                    new HashMap<>());
    long txnId = walletService.customerGiftcardTopupPrepare(request).getPid();

    mockMvc
            .perform(
                    post("/v1/customer/topup/giftcard/cancel")
                            .headers(headers)
                            .content(JSONObject.toJSONString(new ConfirmOrCancelDTO(txnId, null)))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

    CustomerWallet dbCustomerWallet =
            walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    assertThat(transactions.size()).isEqualTo(2);
    assertThat(transactions.get(1).getMetadataByKey("request_path")).isEqualTo("/v1/customer/topup/giftcard/cancel");
  }

  @Test
  public void giftCardTopupRevert() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    long amount = 100;
    CustomerGiftcardTopupPrepare request =
        new CustomerGiftcardTopupPrepare(
            String.valueOf(customerWallet.getUserId()),
            "CASHBACK",
            new Money(Currency.JPY, amount),
            null);
    long txnId = walletService.customerGiftcardTopupPrepare(request).getPid();
    ConfirmOrCancelDTO confirmRequest = new ConfirmOrCancelDTO(txnId, null);
    walletService.customerGiftcardTopupConfirm(confirmRequest);

    mockMvc
        .perform(
            post("/v1/customer/topup/giftcard/revert")
                .headers(headers)
                .content(JSONObject.toJSONString(new RevertDTO(txnId, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    CustomerWallet dbCustomerWallet =
        walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    assertThat(transactions.size()).isEqualTo(2);

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(txnId);
    assertThat(derivedTransactions.size()).isEqualTo(1);
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);
    assertThat(derivedTransactions.get(0).getMetadataByKey("request_path")).isEqualTo("/v1/customer/topup/giftcard/revert");
  }

  @Test
  public void giftCardTopupRevertToPrepaid() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    long amount = 100;
    CustomerGiftcardTopupPrepare request =
        new CustomerGiftcardTopupPrepare(
            String.valueOf(customerWallet.getUserId()),
            "PREPAID",
            new Money(Currency.JPY, amount),
            null);
    long txnId = walletService.customerGiftcardTopupPrepare(request).getPid();
    ConfirmOrCancelDTO confirmRequest = new ConfirmOrCancelDTO(txnId, null);
    walletService.customerGiftcardTopupConfirm(confirmRequest);

    mockMvc
        .perform(
            post("/v1/customer/topup/giftcard/revert")
                .headers(headers)
                .content(JSONObject.toJSONString(new RevertDTO(txnId, null)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    CustomerWallet dbCustomerWallet =
        walletFactory.rebuildCustomerWalletByOwnerId(customerWallet.getUserId());
    assertThat(dbCustomerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.prepaid().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.payment().balance().getCents()).isEqualTo(0);
    assertThat(dbCustomerWallet.incoming().balance().getCents()).isEqualTo(0);

    List<Transaction> transactions = transactionRepository.transactionsOfPid(txnId);
    assertThat(transactions.size()).isEqualTo(2);

    List<DerivedTransaction> derivedTransactions = derivedTransactionRepository.transactionsOfOriginalPid(txnId);
    assertThat(derivedTransactions.size()).isEqualTo(1);
    assertThat(derivedTransactions.get(0).getType()).isEqualTo(DerivedTransactionType.REVERT);
    assertThat(derivedTransactions.get(0).getMetadataByKey("request_path")).isEqualTo("/v1/customer/topup/giftcard/revert");
  }

  @Test
  public void prepare() throws Exception {
    Map<String, String> data = new HashMap<>();
    data.put("order_id", "789");
    data.put("order_type", "acquiring");

    CustomerPrepareDTO customerPrepareDTO =
        new CustomerPrepareDTO(
            String.valueOf(customerWallet.getUserId()),
            String.valueOf(merchantWallet.getUserId()),
            new Money(Currency.JPY, 10),
            data);

    Gson gson = new Gson();
    String payload = gson.toJson(customerPrepareDTO).toString();

    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/prepare")
                    .headers(headers)
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
    final JsonObject json = gson.fromJson(result, JsonObject.class);
    final long pid = json.get("pid").getAsLong();
    Map<String,String> metadata = transactionRepository.transactionsOfPid(pid).get(0).getMetadata();
    Assert.assertTrue(metadata.containsKey("order_id"));
    Assert.assertTrue(metadata.containsKey("order_type"));
    Assert.assertTrue(metadata.containsKey("request_path"));
    Assert.assertTrue(metadata.containsKey("dst_wallet_owner_id"));
  }

  @Test
  public void authorize() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerAuthorizeDTO body =
        new CustomerAuthorizeDTO(
            String.valueOf(customerWallet.getUserId()),
            String.valueOf(merchantWallet.getUserId()),
            new Money(Currency.JPY, 10),
            null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/authorize")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void capture() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    long pid =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 1), null));
    List<Long> pids = new ArrayList<Long>();

    pids.add(pid);
    CustomerCaptureDTO body = new CustomerCaptureDTO(pids, null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/capture")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn()
            .getResponse()
            .getContentAsString();

    pids.add(null);
    body = new CustomerCaptureDTO(pids, null);
    result =
        mockMvc
            .perform(
                post("/v1/customer/payment/capture")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    pids.remove(0);
    body = new CustomerCaptureDTO(pids, null);
    result =
        mockMvc
            .perform(
                post("/v1/customer/payment/capture")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  // https://paypay-corp.rickcloud.jp/jira/browse/PP-2698
  @Test
  public void voidResultInIdempotentResponse() throws Exception {

    idempotencyService.start(idemKey);
    idempotencyService.complete(idemKey, "");

    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    long pid =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 1), null));
    List<Long> pids = new ArrayList<Long>();

    pids.add(pid);
    CustomerCaptureDTO body = new CustomerCaptureDTO(pids, null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/capture")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(result).isEmpty();
  }

  @Test
  public void captureByMerchantWithNonNumericId() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    long pid =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWalletWithNonNumericId.getUserId(), new Money(Currency.JPY, 1), null));
    CustomerCaptureDTO body = new CustomerCaptureDTO(Arrays.asList(pid), null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/capture")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn()
            .getResponse()
            .getContentAsString();

  }

  @Test
  public void captureOptimisticLockNotReturn500() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    long pid =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 1), null));
    CustomerCaptureDTO body = new CustomerCaptureDTO(Arrays.asList(pid), null);
    Mockito.doThrow(new ObjectOptimisticLockingFailureException("fake_optimistic_lock_exception", null)).when(walletApplicationService).captureAccept(Matchers.any(), Matchers.any());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/capture")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.resultInfo.resultCode").value("OPTIMISTIC_LOCK"))
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void reverse() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    long pid =
        walletService.authorize(new CustomerAuthorizeDTO(customerWallet.getUserId(), merchantWallet.getUserId(), new Money(Currency.JPY, 1), null));
    List<Long> pids = new ArrayList<Long>();

    pids.add(pid);
    CustomerReverseDTO body = new CustomerReverseDTO(pids, null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/payment/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    pids.add(null);
    body = new CustomerReverseDTO(pids, null);
    result =
        mockMvc
            .perform(
                post("/v1/customer/payment/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();


    pids.remove(0);
    body = new CustomerReverseDTO(pids, null);
    result =
        mockMvc
            .perform(
                post("/v1/customer/payment/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pRevert() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PDTO body =
        new CustomerP2PDTO(
            String.valueOf(customerWallet.getUserId()),
            String.valueOf(receiverWallet.getUserId()),
            new Money(Currency.JPY, 1),
            new HashMap<>());

    MacroTransaction p2pTxn = walletService.p2p(body);

    Map<String, String> meta = new HashMap<String, String>();
    meta.put("order_id", "1234");

    RevertDTO request = new RevertDTO(p2pTxn.getPid(), meta);

    String result =
        mockMvc
            .perform(
                post("/v1/customer/p2p/revert")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2p() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PDTO body =
        new CustomerP2PDTO(
            String.valueOf(customerWallet.getUserId()),
            String.valueOf(receiverWallet.getUserId()),
            new Money(Currency.JPY, 1),
            null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/p2p")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pConsultValidationError() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PDTO body =
        new CustomerP2PDTO(
            String.valueOf(customerWallet.getUserId()),
            String.valueOf(receiverWallet.getUserId()),
            new Money(Currency.JPY, 1),
            null);
    String payload = JSONObject.toJSONString(body);
    String url = "/v1/customer/p2p/consult";
    String result =
        mockMvc
            .perform(post(url)
                .content(payload.replace("receiver_wallet_owner_id", "receiver_wallet_owner"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.resultInfo.resultCode").value("ILLEGAL_ARGUMENT"))
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pConsult() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PDTO body =
        new CustomerP2PDTO(
            String.valueOf(customerWallet.getUserId()),
            String.valueOf(receiverWallet.getUserId()),
            new Money(Currency.JPY, 1),
            new HashMap<>());
    String url = "/v1/customer/p2p/consult";
    String result =
        mockMvc
            .perform(post(url)
                .content(JSONObject.toJSONString(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.wallet_uuid").isNotEmpty())
            .andExpect(jsonPath("$.wallet_type").value("CUSTOMER"))
            .andExpect(jsonPath("$.accounts").isArray())
            .andExpect(jsonPath("$.accounts.length()").value(10))
            .andExpect(jsonPath("$.accounts[0].type").value("CASHBACK"))
            .andExpect(jsonPath("$.accounts[0].balance").value(0))
            .andExpect(jsonPath("$.accounts[1].type").value("PREPAID"))
            .andExpect(jsonPath("$.accounts[2].type").value("PAYMENT"))
            .andExpect(jsonPath("$.p2p_break_down.prepaid_amount").value(1))
            .andExpect(jsonPath("$.p2p_break_down.emoney_amount").value(0))
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pV2Prepare() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PPrepareDTO body =
        new CustomerP2PPrepareDTO(
                String.valueOf(customerWallet.getUserId()),
                new Money(Currency.JPY, 1),
                null);
    String result =
        mockMvc
            .perform(
                post("/v2/customer/p2p/prepare")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.sender_prepaid_amount").value(1))
            .andExpect(jsonPath("$.sender_emoney_amount").value(0))
            .andExpect(jsonPath("$.created_at").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pV2PrepareThenConfirmWithNonKycUser() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PPrepareDTO customerP2PPrepareDTO =
            new CustomerP2PPrepareDTO(
                    String.valueOf(customerWallet.getUserId()),
                    new Money(Currency.JPY, 1),
                    null);
    long pid = walletService.p2pPrepare(customerP2PPrepareDTO).getPid();

    SerializeConfig.getGlobalInstance()
            .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    CustomerP2PConfirmDTO body = new CustomerP2PConfirmDTO(pid, receiverWallet.getUserId(), null);
    String result =
            mockMvc
                    .perform(
                            post("/v2/customer/p2p/confirm")
                                    .headers(headers)
                                    .content(JSONObject.toJSONString(body))
                                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pid").value(pid))
                    .andExpect(jsonPath("$.receiver_prepaid_amount").value(1))
                    .andExpect(jsonPath("$.receiver_emoney_amount").value(0))
                    .andExpect(jsonPath("$.created_at").isNotEmpty())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
  }

  @Test
  public void p2pV2PrepareThenConfirmWithKycUser() throws Exception {
    val kycSender = (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    walletService.customerDeposit(new CustomerDepositDTO(kycSender.getUserId(), new Money(10), null));
    walletService.createEmoneyAccount(new CreateCustomerEmoneyAccountDTO(kycSender.getUserId(), new HashMap<>()));
    walletService.customerDeposit(new CustomerDepositDTO(kycSender.getUserId(), new Money(10), null));

    val kycReceiver = (CustomerWallet) walletService.createCustomerWallet(new CreateCustomerWalletDTO(String.valueOf(UuidGenerator.getUID())));
    walletService.createEmoneyAccount(new CreateCustomerEmoneyAccountDTO(kycReceiver.getUserId(), new HashMap<>()));

    CustomerP2PPrepareDTO customerP2PPrepareDTO =
        new CustomerP2PPrepareDTO(
            String.valueOf(kycSender.getUserId()),
            new Money(Currency.JPY, 11),
            null);
    long pid = walletService.p2pPrepare(customerP2PPrepareDTO).getPid();

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    CustomerP2PConfirmDTO body = new CustomerP2PConfirmDTO(pid, kycReceiver.getUserId(), null);
    String result =
        mockMvc
            .perform(
                post("/v2/customer/p2p/confirm")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").value(pid))
            .andExpect(jsonPath("$.receiver_prepaid_amount").value(10))
            .andExpect(jsonPath("$.receiver_emoney_amount").value(1))
            .andExpect(jsonPath("$.created_at").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pV2PrepareConfirmThenRevert() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PPrepareDTO customerP2PPrepareDTO =
        new CustomerP2PPrepareDTO(
            String.valueOf(customerWallet.getUserId()),
            new Money(Currency.JPY, 1),
            null);
    long pid = walletService.p2pPrepare(customerP2PPrepareDTO).getPid();
    walletService.p2pConfirm(new CustomerP2PConfirmDTO(pid, receiverWallet.getUserId(), null));

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;


    RevertDTO body = new RevertDTO(pid, null);
    String result =
        mockMvc
            .perform(
                post("/v2/customer/p2p/revert")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.created_at").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pV2PrepareThenCancel() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PPrepareDTO customerP2PPrepareDTO =
        new CustomerP2PPrepareDTO(
            String.valueOf(customerWallet.getUserId()),
            new Money(Currency.JPY, 1),
            null);
    long pid = walletService.p2pPrepare(customerP2PPrepareDTO).getPid();

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    ConfirmOrCancelDTO body = new ConfirmOrCancelDTO(pid, null);
    String result =
        mockMvc
            .perform(
                post("/v2/customer/p2p/cancel")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").value(pid))
            .andExpect(jsonPath("$.created_at").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void p2pV2ConsultValidationError() throws Exception {
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    String url = "/v2/customer/p2p/consult";
    String result =
            mockMvc
                    .perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.resultInfo.resultCode").value("ILLEGAL_ARGUMENT"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
  }

  @Test
  public void p2pV2Consult() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    walletService.customerDeposit(new CustomerDepositDTO(customerWallet.getUserId(), amount, null));
    CustomerP2PPrepareDTO body =
            new CustomerP2PPrepareDTO(
                    String.valueOf(customerWallet.getUserId()),
                    new Money(Currency.JPY, 1),
                    null);
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    String url = "/v2/customer/p2p/consult";
    String result =
            mockMvc
                    .perform(post(url)
                            .content(JSONObject.toJSONString(body))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.wallet_uuid").isNotEmpty())
                    .andExpect(jsonPath("$.wallet_type").value("CUSTOMER"))
                    .andExpect(jsonPath("$.accounts").isArray())
                    .andExpect(jsonPath("$.accounts.length()").value(10))
                    .andExpect(jsonPath("$.accounts[0].type").value("CASHBACK"))
                    .andExpect(jsonPath("$.accounts[0].balance").value(0))
                    .andExpect(jsonPath("$.accounts[1].type").value("PREPAID"))
                    .andExpect(jsonPath("$.accounts[2].type").value("PAYMENT"))
                    .andExpect(jsonPath("$.p2p_break_down.prepaid_amount").value(1))
                    .andExpect(jsonPath("$.p2p_break_down.emoney_amount").value(0))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
  }

  @Test
  public void cashback() throws Exception {
    CustomerCashbackDTO body = new CustomerCashbackDTO(campaignToken, customerWallet.getUserId(), amount, null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/deposit")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackV3Badrequest() throws Exception {
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, "CASHBACK_PENDING", new HashMap<>());

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    String result =
        mockMvc
            .perform(
                post("/v3/customer/cashback/deposit")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().is4xxClientError())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(result).contains("pending account and activation date must come together");
  }

  @Test
  public void cashbackV3Prepaid() throws Exception {
    Map<String, String> meta = new HashMap<>();
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, "PREPAID", meta);

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    String result =
        mockMvc
            .perform(
                post("/v3/customer/cashback/deposit")
                .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(amount.getCents());
  }

  @Test
  public void cashbackV3Pending() throws Exception {
    Map<String, String> meta = new HashMap<>();
    meta.put("activation_date", "2018-12-31T00:00:00Z");
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, "CASHBACK_PENDING", meta);

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    String result =
        mockMvc
            .perform(
                post("/v3/customer/cashback/deposit")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(customerWallet.getCashbackPending().balance().getCents()).isEqualTo(amount.getCents());
  }

  @Test
  public void cashbackV3PendingThenRelease() throws Exception {
    Map<String, String> meta = new HashMap<>();
    meta.put("activation_date", "2018-12-31T00:00:00Z");
    meta.put("attributed_campaign_id", "123");
    meta.put("registration_id", "456");
    meta.put("other", "789");
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, "CASHBACK_PENDING", meta);


    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    final long pid = walletService.cashbackV3(body).getPid();
    ConfirmOrCancelDTO followUp = new ConfirmOrCancelDTO(Long.valueOf(pid), null);
    long balanceC = customerWallet.cashback().balance().getCents();
    long balanceCE = customerWallet.cashbackExpirable().balance().getCents();

    String result =
        mockMvc
            .perform(
                post("/v3/customer/cashback/release")
                    .headers(headers)
                    .content(JSONObject.toJSONString(followUp))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(customerWallet.cashback().balance().getCents()).isEqualTo(amount.getCents() + balanceC);
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(balanceCE);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    Transaction lastTransaction = transactions.get(transactions.size()-1);
    assertThat(lastTransaction.getMetadataByKey("request_path")).isEqualTo("/v3/customer/cashback/release");
    assertThat(lastTransaction.getMetadataByKey("attributed_campaign_id")).isEqualTo("123");
    assertThat(lastTransaction.getMetadataByKey("registration_id")).isEqualTo("456");
    assertThat(lastTransaction.getMetadataByKey("other")).isNull();
  }

  @Test
  public void cashbackV3PendingThenBatchRelease() throws Exception {
    Map<String, String> meta = new HashMap<>();
    meta.put("activation_date", "2018-12-31T00:00:00Z");
    meta.put("attributed_campaign_id", "123");
    meta.put("registration_id", "456");
    meta.put("other", "789");
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, "CASHBACK_PENDING", meta);


    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    final long pid = walletService.cashbackV3(body).getPid();
    List<BatchRequestElement<ConfirmOrCancelDTO>> requests = Arrays.asList(new BatchRequestElement<>(String.valueOf(pid), new ConfirmOrCancelDTO(pid, null)));
    long balanceC = customerWallet.cashback().balance().getCents();

    String result =
        mockMvc
            .perform(
                post("/v3/customer/cashback/batch-release")
                    .headers(headers)
                    .content(JSONObject.toJSONString(requests))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(JSONPath.read(result, "$[0].result.pid")).isEqualTo(String.valueOf(pid));
    assertThat(customerWallet.cashback().balance().getCents()).isEqualTo(amount.getCents() + balanceC);
    List<Transaction> transactions = transactionRepository.transactionsOfPid(pid);
    Transaction lastTransaction = transactions.get(transactions.size()-1);
    assertThat(lastTransaction.getMetadataByKey("request_path")).isEqualTo("/v3/customer/cashback/batch-release");
    assertThat(lastTransaction.getMetadataByKey("attributed_campaign_id")).isEqualTo("123");
    assertThat(lastTransaction.getMetadataByKey("registration_id")).isEqualTo("456");
    assertThat(lastTransaction.getMetadataByKey("other")).isNull();
  }

  @Test
  public void partialCashbackReverseFromPending() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    Map<String, String> meta = new HashMap<>();
    meta.put("activation_date", "2018-12-31T00:00:00Z");
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, "CASHBACK_PENDING", meta);


    final long depositPid = walletService.cashbackV3(body).getPid();

    meta = new HashMap<>();
    meta.put("code", "clm_refund");
    Money partialReverseAmount = new Money(Currency.JPY, 50);
    CustomerCashbackReverseDTO request =
        new CustomerCashbackReverseDTO(Long.valueOf(depositPid), partialReverseAmount, meta);

    long balanceC = customerWallet.cashback().balance().getCents();
    long balanceP = customerWallet.cashbackPending().balance().getCents();
    assertThat(balanceP).isEqualTo(amount.getCents());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.charged").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(customerWallet.cashback().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet
        .cashbackPending()
        .balance()
        .getCents()).isEqualTo(balanceP - 50);

    assertThat(JSONPath.read(result, "$.pid")).isNotEqualTo(depositPid);
    assertThat(JSONPath.read(result, "$.charged.cents")).isEqualTo(new Integer(partialReverseAmount.getCents().toString()));
    assertThat(JSONPath.read(result, "$.reversed")).isNull();
  }

  @Test
  public void CashbackReverseFromPending() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    Map<String, String> depositMeta = new HashMap<>();
    depositMeta.put("activation_date", "2018-12-31T00:00:00Z");
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, AccountType.CASHBACK_PENDING.name(), depositMeta);
    final long depositPid = walletService.cashbackV3(body).getPid();

    CustomerCashbackReverseDTO request =
        new CustomerCashbackReverseDTO(Long.valueOf(depositPid), amount, new HashMap<>());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.charged").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(JSONPath.read(result, "$.pid")).isEqualTo(depositPid);
    assertThat(JSONPath.read(result, "$.charged.cents")).isEqualTo(new Integer(amount.getCents().toString()));
    assertThat(JSONPath.read(result, "$.reversed")).isNull();
  }

  @Test
  public void cashbackReverseFromPrepaid() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, AccountType.PREPAID.name(), new HashMap<>());
    final long depositPid = walletService.cashbackV3(body).getPid();

    CustomerCashbackReverseDTO request =
        new CustomerCashbackReverseDTO(Long.valueOf(depositPid), amount, new HashMap<>());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.charged").isNotEmpty())
            .andExpect(jsonPath("$.reversed").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(JSONPath.read(result, "$.pid")).isNotEqualTo(depositPid);
    assertThat(JSONPath.read(result, "$.charged.cents")).isEqualTo(new Integer(amount.getCents().toString()));
    assertThat(JSONPath.read(result, "$.reversed[0].account_type")).isEqualTo(AccountType.PREPAID.name());
    assertThat(JSONPath.read(result, "$.reversed[0].amount.cents")).isEqualTo(new Integer(amount.getCents().toString()));
  }

  @Test
  public void cashbackReverseFromCashback() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, AccountType.CASHBACK.name(), new HashMap<>());
    final long depositPid = walletService.cashbackV3(body).getPid();

    CustomerCashbackReverseDTO request =
        new CustomerCashbackReverseDTO(Long.valueOf(depositPid), amount, new HashMap<>());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.charged").isNotEmpty())
            .andExpect(jsonPath("$.reversed").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(JSONPath.read(result, "$.pid")).isNotEqualTo(depositPid);
    assertThat(JSONPath.read(result, "$.charged.cents")).isEqualTo(new Integer(amount.getCents().toString()));
    assertThat(JSONPath.read(result, "$.reversed[0].account_type")).isEqualTo(AccountType.CASHBACK.name());
    assertThat(JSONPath.read(result, "$.reversed[0].amount.cents")).isEqualTo(new Integer(amount.getCents().toString()));
  }

  @Test
  public void cashbackReverseFromCashbackExpirable() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    Map<String, String> depositMeta = new HashMap<>();
    Date futureDate = DateUtils.dateFromDays("3");
    depositMeta.put("expiry_date", DateUtils.dateToString(futureDate));
    CustomerCashbackV3DTO body = new CustomerCashbackV3DTO(
        campaignToken, customerWallet.getUserId(), amount, AccountType.CASHBACK_EXPIRABLE.name(), depositMeta);
    final long depositPid = walletService.cashbackV3(body).getPid();

    CustomerCashbackReverseDTO request =
        new CustomerCashbackReverseDTO(Long.valueOf(depositPid), amount, new HashMap<>());

    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.charged").isNotEmpty())
            .andExpect(jsonPath("$.reversed").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(JSONPath.read(result, "$.pid")).isNotEqualTo(depositPid);
    assertThat(JSONPath.read(result, "$.charged.cents")).isEqualTo(new Integer(amount.getCents().toString()));
    assertThat(JSONPath.read(result, "$.reversed[0].account_type")).isEqualTo(AccountType.CASHBACK_EXPIRABLE.name());
    assertThat(JSONPath.read(result, "$.reversed[0].amount.cents")).isEqualTo(new Integer(amount.getCents().toString()));
  }

  @Test
  public void cashbackDepositV4_should_return_accepted() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"PREPAID\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                        + "\"campaign_token\":\"campaign1\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_accepted_and_should_be_processed_with_no_budget_constraint() throws Exception {
    Account campaign = walletFactory.rebuildSystemWallet().campaign(campaignToken);
    long beforeCustomerBalance = customerWallet.prepaid().balance().getCents();
    long beforeCampaignBalance = campaign.balance().getCents();

    // disabled budget constraint
    campaign.setBudgetConstraintDisabled(true);

    String result =
            mockMvc
                    .perform(
                            post("/v4/customer/cashback/deposit")
                                    .headers(headers)
                                    .content(String.format("{\"account_type\":\"PREPAID\","
                                            + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                                            + "\"campaign_token\":\"%s\","
                                            + "\"customer_wallet_owner_id\":\"%s\","
                                            + "\"metadata\":{\"key\":\"value\"}}", campaignToken, customerWallet.getUserId()))
                                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isAccepted())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

    assertThat(customerWallet.prepaid().balance().getCents()).isEqualTo(beforeCustomerBalance + 100);
    assertThat(campaign.balance().getCents()).isEqualTo(beforeCampaignBalance); // should be same
  }

  @Test
  public void cashbackDepositV4_to_cashbackpending_with_past_acitivation_date_should_return_accepted() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"CASHBACK_PENDING\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                        + "\"campaign_token\":\"campaign1\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"activation_date\":\"2000-01-01T00:00:00\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_bad_request_without_campaign_token() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"PREPAID\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_bad_request_with_negative_money() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"PREPAID\","
                        + "\"amount\":{\"cents\":-100,\"currency\":\"JPY\"},"
                        + "\"campaign_token\":\"campaign1\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_bad_request_with_non_existing_currency() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"PREPAID\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"NOWHERE\"},"
                        + "\"campaign_token\":\"campaign1\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_bad_request_with_non_existing_account_type() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"NOWHERE\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                        + "\"campaign_token\":\"campaign1\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_bad_request_with_non_existing_campaign() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"PREPAID\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                        + "\"campaign_token\":\"NOWHERE\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", customerWallet.getUserId()))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV4_should_return_bad_request_with_not_existed_wallet_owner_id() throws Exception {
    String result =
        mockMvc
            .perform(
                post("/v4/customer/cashback/deposit")
                    .headers(headers)
                    .content(String.format("{\"account_type\":\"PREPAID\","
                        + "\"amount\":{\"cents\":100,\"currency\":\"JPY\"},"
                        + "\"campaign_token\":\"campaign1\","
                        + "\"customer_wallet_owner_id\":\"%s\","
                        + "\"metadata\":{\"key\":\"value\"}}", "not_existed_owner_id" ))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }


  @Test
  public void cashbackDepositV5_should_return_accepted() throws Exception {
    String body = "{\n" +
        "  \"campaign_token\": \"campaign1\",\n" +
        "  \"customer_wallet_owner_id\": \""+ customerWallet.getUserId() +"\",\n" +
        "  \"amount\": {\n" +
        "    \"currency\": \"JPY\",\n" +
        "    \"cents\": \"100\"\n" +
        "  },\n" +
        "  \"account_type\": \"PREPAID\",\n" +
        "  \"partition_key\": \"partitionKey\",\n" +
        "  \"cashback_id\": \"cashbackId\",\n" +
        "  \"metadata\" : {\n" +
        "    \"key\": \"value\"\n" +
        "  }\n" +
        "}";
    String result =
        mockMvc
            .perform(
                post("/v5/customer/cashback/deposit")
                    .headers(headers)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV5_response_createAt_should_match_idemservice_object() throws Exception {
    String body = "{\n" +
            "  \"campaign_token\": \"campaign1\",\n" +
            "  \"customer_wallet_owner_id\": \""+ customerWallet.getUserId() +"\",\n" +
            "  \"amount\": {\n" +
            "    \"currency\": \"JPY\",\n" +
            "    \"cents\": \"100\"\n" +
            "  },\n" +
            "  \"account_type\": \"PREPAID\",\n" +
            "  \"partition_key\": \"partitionKey\",\n" +
            "  \"cashback_id\": \"cashbackId\",\n" +
            "  \"metadata\" : {\n" +
            "    \"key\": \"value\"\n" +
            "  }\n" +
            "}";
    String result =
            mockMvc
              .perform(
                      post("/v5/customer/cashback/deposit")
                              .headers(headers)
                              .content(body)
                              .contentType(MediaType.APPLICATION_JSON_UTF8))
              .andExpect(status().isAccepted())
              .andReturn()
              .getResponse()
              .getContentAsString();

  }

  @Test
  public void cashbackDepositV5_should_return_bad_request_without_partition_key() throws Exception {
    String body = "{\n" +
        "  \"campaign_token\": \"campaign1\",\n" +
        "  \"customer_wallet_owner_id\": \""+ customerWallet.getUserId() +"\",\n" +
        "  \"amount\": {\n" +
        "    \"currency\": \"JPY\",\n" +
        "    \"cents\": \"100\"\n" +
        "  },\n" +
        "  \"account_type\": \"PREPAID\",\n" +
        "  \"cashback_id\": \"cashbackId\",\n" +
        "  \"metadata\" : {\n" +
        "    \"key\": \"value\"\n" +
        "  }\n" +
        "}";
    String result =
        mockMvc
            .perform(
                post("/v5/customer/cashback/deposit")
                    .headers(headers)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackDepositV5_should_return_bad_request_without_cashback_id() throws Exception {
    String body = "{\n" +
        "  \"campaign_token\": \"campaign1\",\n" +
        "  \"customer_wallet_owner_id\": \""+ customerWallet.getUserId() +"\",\n" +
        "  \"amount\": {\n" +
        "    \"currency\": \"JPY\",\n" +
        "    \"cents\": \"100\"\n" +
        "  },\n" +
        "  \"account_type\": \"PREPAID\",\n" +
        "  \"partition_key\": \"partitionKey\",\n" +
        "  \"metadata\" : {\n" +
        "    \"key\": \"value\"\n" +
        "  }\n" +
        "}";
    String result =
        mockMvc
            .perform(
                post("/v5/customer/cashback/deposit")
                    .headers(headers)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void cashbackReverse() throws Exception {
    long pid = walletService.cashback(new CustomerCashbackDTO(campaignToken, customerWallet.getUserId(), amount, null));
    CustomerCashbackReverseDTO body = new CustomerCashbackReverseDTO(pid, amount, null);
    String result =
        mockMvc
            .perform(
                post("/v1/customer/cashback/reverse")
                    .headers(headers)
                    .content(JSONObject.toJSONString(body))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid").isNotEmpty())
            .andExpect(jsonPath("$.charged").isNotEmpty())
            .andExpect(jsonPath("$.reversed").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  @Test
  public void topupCustomerCampaign() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    CampaignTopupDTO body = new CampaignTopupDTO(campaignToken, String.valueOf(customerWallet.getUserId()), "budgetToken", new Money(Currency.JPY, 100), new HashMap<>());
    mockMvc.perform(
        post("/v1/customer/campaign/funding/system")
            .headers(headers)
            .content(JSONObject.toJSONString(body))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pid").isNotEmpty())
        .andExpect(jsonPath("$.created_at").isNotEmpty())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void customerCampaignDeposit() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, amount, new HashMap<>()));

    CampaignDepositDTO body = new CampaignDepositDTO(budgetToken, customerWallet.getUserId(), receiverWallet.getUserId(), amount, new HashMap<>());
    mockMvc.perform(
        post("/v1/customer/campaign/deposit")
            .headers(headers)
            .content(JSONObject.toJSONString(body))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pid").isNotEmpty())
        .andExpect(jsonPath("$.created_at").isNotEmpty())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void customerCampaignFundingReverse() throws Exception {

    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    MacroTransaction transaction = walletService.topupCustomerCampaign(new CampaignTopupDTO(campaignToken, customerWallet.getUserId(), budgetToken, amount, new HashMap<>()));
    CampaignTopupReverseDTO request = new CampaignTopupReverseDTO(transaction.getPid(), budgetToken, amount, new HashMap<>());

    mockMvc.perform(
        post("/v1/customer/campaign/funding/reverse")
            .headers(headers)
            .content(JSONObject.toJSONString(request))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pid").isNotEmpty())
        .andExpect(jsonPath("$.created_at").isNotEmpty())
        .andExpect(jsonPath("$.budget_token").value(budgetToken))
        .andExpect(jsonPath("$.reversed.currency").value(amount.getCurrency().toString()))
        .andExpect(jsonPath("$.reversed.cents").value(amount.getCents()))
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void createCustomerEmoneyAccount() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    CreateCustomerEmoneyAccountDTO request = new CreateCustomerEmoneyAccountDTO(customerWallet.getUserId(), new HashMap<>());
    String response = mockMvc.perform(
        post("/v1/customer/accounts/emoney")
            .headers(headers)
            .content(JSONObject.toJSONString(request))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.uuid").isNotEmpty())
        .andReturn()
        .getResponse()
        .getContentAsString();

    AccountDTO expected = new AccountDTO("not assert", "EMONEY", true, "JPY", 0L);
    Gson gson = new Gson();
    AccountDTO accountDTO = gson.fromJson(response, AccountDTO.class);
    assertThat(accountDTO).isEqualToIgnoringGivenFields(expected, "uuid");
  }

  @Test
  public void payoutConfirm() throws Exception {
    val payoutAmount = 300;
    val commissionAmount = 100;
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), new Money(payoutAmount), null));
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), new Money(commissionAmount), null));

    CustomerPayoutPrepareDTO request =
        new CustomerPayoutPrepareDTO(
            new Money(payoutAmount), new Money(commissionAmount), kycCustomerWallet.getUserId(), new HashMap<>());
    val macroTransaction = walletService.customerPayoutPrepare(request);
    val txnId = macroTransaction.getPid();
    val body = new ConfirmOrCancelDTO(txnId, null);

    String result = mockMvc
        .perform(
            post("/v1/customer/payout/confirm")
                .headers(headers)
                .content(JSONObject.toJSONString(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pid").isNotEmpty())
        .andExpect(jsonPath("$.created_at").isNotEmpty())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void payoutConfirmShouldReturnBadRequestWithNonExistingPid() throws Exception {
    val body = new ConfirmOrCancelDTO(0L, null);

    String result = mockMvc
        .perform(
            post("/v1/customer/payout/confirm")
                .headers(headers)
                .content(JSONObject.toJSONString(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void payoutCancel() throws Exception {
    val payoutAmount = 300;
    val commissionAmount = 100;
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), new Money(payoutAmount), null));
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), new Money(commissionAmount), null));

    CustomerPayoutPrepareDTO request =
        new CustomerPayoutPrepareDTO(
            new Money(payoutAmount), new Money(commissionAmount), kycCustomerWallet.getUserId(), new HashMap<>());
    val macroTransaction = walletService.customerPayoutPrepare(request);
    val txnId = macroTransaction.getPid();
    val body = new ConfirmOrCancelDTO(txnId, null);
// {"pid":"579364888929984562","created_at":"2019-07-26T07:37:56.140+0000"}"
    String result = mockMvc
        .perform(
            post("/v1/customer/payout/cancel")
                .headers(headers)
                .content(JSONObject.toJSONString(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pid").value(txnId))
        .andExpect(jsonPath("$.created_at").isNotEmpty())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void payoutRevert() throws Exception {
    val payoutAmount = 300;
    val commissionAmount = 100;
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), new Money(payoutAmount), null));
    walletService.customerDeposit(new CustomerDepositDTO(kycCustomerWallet.getUserId(), new Money(commissionAmount), null));

    val prepareDTO =
        new CustomerPayoutPrepareDTO(
            new Money(payoutAmount), new Money(commissionAmount), kycCustomerWallet.getUserId(), new HashMap<>());
    val macroTransaction = walletService.customerPayoutPrepare(prepareDTO);
    val confirmDTO =
        new ConfirmOrCancelDTO(macroTransaction.getPid(), null);
    walletService.customerPayoutConfirm(confirmDTO);

    val txnId = macroTransaction.getPid();
    val body = new RevertDTO(txnId, null);

    String result = mockMvc
        .perform(
            post("/v1/customer/payout/revert")
                .headers(headers)
                .content(JSONObject.toJSONString(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pid").isNotEmpty())
        .andExpect(jsonPath("$.created_at").isNotEmpty())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  @Test
  public void customerCashbackPrepareForExpiryTest() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    Date futureDate = DateUtils.dateFromDays("3");
    MerchantCampaignCashbackDepositDTO depositRequest = getCashbackRequestData(AccountType.CASHBACK_EXPIRABLE.name(),"", DateUtils.dateToString(futureDate) , "", String.valueOf(UuidGenerator.getUID()));

    // deposit of 100
    TransactionV2DTO depositResponse = walletService.processMerchantCampaignCashbackDeposit(depositRequest);

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerId);
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(100);
    assertThat(customerWallet.expired().balance().getCents()).isEqualTo(0);


    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerId, DateUtils.dateToString(futureDate), new HashMap<>());

    String result =
        mockMvc.perform(post("/v1/customer/cashback/expire/prepare")
            .headers(headers)
            .content(JSONObject.toJSONString(request))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertThat(JSONPath.read(result, "$[0].pid")).isNotNull();
    assertThat(JSONPath.read(result, "$[1].pid")).isNull();
    customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerId);
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet.expired().balance().getCents()).isEqualTo(100);
  }

  @Test
  public void customerCashbackPrepareForExpiryWithTwoDepositsTest() throws Exception {
    SerializeConfig.getGlobalInstance()
        .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

    Date futureDate = DateUtils.dateFromDays("3");
    MerchantCampaignCashbackDepositDTO depositRequest = getCashbackRequestData(AccountType.CASHBACK_EXPIRABLE.name(),"", DateUtils.dateToString(futureDate) , "", String.valueOf(UuidGenerator.getUID()));

    // deposit of 100
    TransactionV2DTO depositResponse = walletService.processMerchantCampaignCashbackDeposit(depositRequest);

    CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerId);
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(100);
    assertThat(customerWallet.expired().balance().getCents()).isEqualTo(0);

    // second deposit of 100
    depositResponse = walletService.processMerchantCampaignCashbackDeposit(depositRequest);

    customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerId);
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(200);
    assertThat(customerWallet.expired().balance().getCents()).isEqualTo(0);

    PrepareCustomerCashbackExpiryDTO request = new PrepareCustomerCashbackExpiryDTO(customerId, DateUtils.dateToString(futureDate), new HashMap<>());

    String result =
        mockMvc.perform(post("/v1/customer/cashback/expire/prepare")
            .headers(headers)
            .content(JSONObject.toJSONString(request))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // one transaction per deposit transaction
    assertThat(JSONPath.read(result, "$[0].pid")).isNotNull();
    assertThat(JSONPath.read(result, "$[1].pid")).isNotNull();
    assertThat(JSONPath.read(result, "$[2].pid")).isNull();
    customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerId);
    assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(0);
    assertThat(customerWallet.expired().balance().getCents()).isEqualTo(200);
  }

  @Test
  public void customerCashbackConfirmExpireTest() throws Exception {
      SerializeConfig.getGlobalInstance()
          .propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

      Date futureDate = DateUtils.dateFromDays("3");
      String campaignToken = String.valueOf(UuidGenerator.getUID());
      MerchantCampaignCashbackDepositDTO depositRequest = getCashbackRequestData(AccountType.CASHBACK_EXPIRABLE.name(),"", DateUtils.dateToString(futureDate) , "", campaignToken);

      // deposit of 100
      TransactionV2DTO depositResponse = walletService.processMerchantCampaignCashbackDeposit(depositRequest);

      CustomerWallet customerWallet = walletFactory.rebuildCustomerWalletByOwnerId(customerId);
      MerchantWallet merchantWallet = walletFactory.rebuildMerchantWalletByOwnerId(merchantId);
      assertThat(customerWallet.cashbackExpirable().balance().getCents()).isEqualTo(100);
      assertThat(customerWallet.expired().balance().getCents()).isEqualTo(0);
      assertThat(merchantWallet.campaign(campaignToken).get().balance().getCents()).isEqualTo(9900L);

      PrepareCustomerCashbackExpiryDTO expirePrepareReq = new PrepareCustomerCashbackExpiryDTO(customerId, DateUtils.dateToString(futureDate), new HashMap<>());
      List<TransactionV2DTO> pidList = walletService.expireCashbackPrepare(expirePrepareReq);
      assertThat(pidList.size()).isEqualTo(1);

      ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(Long.parseLong(pidList.get(0).getPid()), new HashMap<>());

      String result =
          mockMvc.perform(post("/v1/customer/cashback/expire/confirm")
              .headers(headers)
              .content(JSONObject.toJSONString(request))
              .contentType(MediaType.APPLICATION_JSON_UTF8))
              .andExpect(status().isAccepted())
              .andReturn()
              .getResponse()
              .getContentAsString();
  }

  @Test
  public void comfirmExpireWithoutPrepareTest() throws Exception {
      ConfirmOrCancelDTO request = new ConfirmOrCancelDTO(UuidGenerator.getUID(), new HashMap<>());
      mockMvc.perform(post("/v1/customer/cashback/expire/confirm")
          .headers(headers)
          .content(JSONObject.toJSONString(request))
          .contentType(MediaType.APPLICATION_JSON_UTF8))
          .andExpect(status().isBadRequest())
          .andReturn()
          .getResponse()
          .getContentAsString();
  }

  private MerchantCampaignCashbackDepositDTO getCashbackRequestData(String type, String activationDate, String expiryDate, String relativeDate, String campaignToken) {

    CreateMerchantCampaignAccountDTO campaignAccountDTO = new CreateMerchantCampaignAccountDTO(merchantId, campaignToken, null);
    walletService.createMerchantCampaignAccount(campaignAccountDTO);

    long campaignFundingAmount = 10000;
    Money campaignFund = new Money(Currency.JPY, campaignFundingAmount);
    MerchantCampaignTopupDTO topupRequest = new MerchantCampaignTopupDTO(campaignToken, merchantId, campaignFund, null );
    walletService.merchantCampaignAccountFunding(topupRequest);

    MerchantCampaignCashbackDepositDTO request = new MerchantCampaignCashbackDepositDTO();
    request.customer_wallet_owner_id  = customerId;
    request.merchantWalletOwnerId = merchantId;
    if (type != null) {
      request.setAccountType(type);
    }
    request.amount = new Money(Currency.JPY, 100);
    request.campaign_token = campaignToken;

    request.metadata = buildMetadata(activationDate, expiryDate, relativeDate);
    return request;
  }

  private Map<String, String> buildMetadata(String activationDate, String expiryDate, String relativeExpiry) {
    Map<String, String> metadata = new HashMap<>();

    if(!activationDate.isEmpty()) {
      metadata.put("activation_date", activationDate);
    }
    if(!expiryDate.isEmpty()) {
      metadata.put("expiry_date", expiryDate);
    }
    if(!relativeExpiry.isEmpty()) {
      metadata.put("relative_expiry", relativeExpiry);
    }
    return metadata;
  }

}