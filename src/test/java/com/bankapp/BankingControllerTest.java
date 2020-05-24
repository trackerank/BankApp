package com.bankapp;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.bankapp.controller.BankingController;
import com.bankapp.model.Branch;
import com.bankapp.service.BankingService;

@RunWith(SpringRunner.class)
@WebMvcTest(BankingController.class)
public class BankingControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private BankingService bankingService;
	
	
	@Test
	public void getAllBranches()
	  throws Exception {
	     
		Branch branch = new Branch();
		branch.setBranchId("1");
	 
	    //List<Object> branches = Arrays.asList(branch);
	 
	    //given(bankingService.getAllBranches()).willReturn(branches);
	 
	    mvc.perform(get("/branch/getAll")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk());
	}
	
	@Test
	public void getCustomerByPan() throws Exception {
		
		String panNumber = "AB12";
		when(bankingService.getCustomerByPan(panNumber)).thenReturn("AB12");
		mvc.perform(get("/customer/{panNumber}")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("AB12")));
	}
	
}
