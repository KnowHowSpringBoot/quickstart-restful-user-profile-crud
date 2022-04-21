package org.ujar.sample.crudrest.web;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.ujar.sample.crudrest.entity.Account;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

  private final MockMvc mvc;

  public AccountControllerTest(@Autowired MockMvc mvc) {
    this.mvc = mvc;
  }

  @Test
  public void create() throws Exception {
    var account = new Account();
    account.setActive(true);

    var responseBody = createNewAccount(account);
    assertAccountEquals(account, responseBody);
    deleteAccount(responseBody.getId());

    account.setActive(false);

    responseBody = createNewAccount(account);
    assertAccountEquals(account, responseBody);
    deleteAccount(responseBody.getId());
  }

  @Test
  public void findById() throws Exception {
    var gson = new Gson();
    var account = new Account();
    account.setActive(true);

    var responseBody = createNewAccount(account);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/account/" + responseBody.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    responseBody = gson.fromJson(result.getResponse().getContentAsString(), Account.class);

    assertAccountEquals(account, responseBody);
    deleteAccount(responseBody.getId());
  }

  @Test
  public void findAll() throws Exception {

    var account = new Account();
    account.setActive(true);

    var numberOfRecords = 3;

    var createdAccounts = new ArrayList<Account>();
    for (int i = 0; i < numberOfRecords; i++) {
      createdAccounts.add(createNewAccount(account));
    }

    mvc.perform(MockMvcRequestBuilders.get("/account")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\n"
            + "  \"content\":[\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    },\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    },\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    },\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    },\n"
            + "    {\n"
            + "      \"active\":true\n"
            + "    }\n"
            + "  ],\n"
            + "  \"pageable\":{\n"
            + "    \"sort\":{\n"
            + "      \"empty\":true,\n"
            + "      \"unsorted\":true,\n"
            + "      \"sorted\":false\n"
            + "    },\n"
            + "    \"offset\":0,\n"
            + "    \"pageSize\":100,\n"
            + "    \"pageNumber\":0,\n"
            + "    \"unpaged\":false,\n"
            + "    \"paged\":true\n"
            + "  },\n"
            + "  \"last\":true,\n"
            + "  \"totalElements\":5,\n"
            + "  \"totalPages\":1,\n"
            + "  \"size\":100,\n"
            + "  \"number\":0,\n"
            + "  \"sort\":{\n"
            + "    \"empty\":true,\n"
            + "    \"unsorted\":true,\n"
            + "    \"sorted\":false\n"
            + "  },\n"
            + "  \"first\":true,\n"
            + "  \"numberOfElements\":3,\n"
            + "  \"empty\":false\n"
            + "}"
        ));
    for (Account singleAccount : createdAccounts) {
      deleteAccount(singleAccount.getId());
    }
  }

  @Test
  public void update() throws Exception {
    var account = new Account();
    account.setActive(true);

    var responseBody = createNewAccount(account);

    responseBody = getAccountById(responseBody.getId());
    assertAccountEquals(account, responseBody);
    deleteAccount(responseBody.getId());
  }

  @Test
  public void delete() throws Exception {
    var account = new Account();
    account.setActive(true);

    var responseBody = createNewAccount(account);

    deleteAccount(responseBody.getId());
    responseBody = getAccountById(responseBody.getId());
    assertNull(responseBody);
  }

  private Account createNewAccount(Account account) throws Exception {
    var gson = new Gson();

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(gson.toJson(account))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), Account.class);
  }

  private Account getAccountById(Long id) throws Exception {
    var gson = new Gson();

    var result = mvc.perform(MockMvcRequestBuilders.get("/account/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    return gson.fromJson(result.getResponse().getContentAsString(), Account.class);
  }

  private void deleteAccount(Long id) throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/account/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  private void assertAccountEquals(Account account, Account responseBody) {
    assertNotNull(responseBody);
    assertNotNull(responseBody.getId());
    assertEquals(responseBody.isActive(), account.isActive());
  }

}
