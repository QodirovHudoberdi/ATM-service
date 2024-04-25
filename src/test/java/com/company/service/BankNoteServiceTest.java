package com.company.service;
import com.company.dto.request.BankNoteReqDto;
import com.company.entity.BankNote;
import com.company.repository.BankNoteRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BankNoteServiceTest {
    @Mock
    private BankNoteRepository bankNoteRepository;
    @Mock
    private CheckService checkService;
 @Mock
    private BankNoteReqDto bankNoteReqDto;
 @Mock
    private HttpServletRequest httpServletRequest;
 @Mock
 private BankNoteService bankNoteService;
    @Test
    public void testDeleteBankNote() {
        // Create a mock HttpServletRequest
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        // Create a BankNote instance
        BankNote bankNote = new BankNote();
        bankNote.setAmount(100);

        // Configure the mock objects
        when(bankNoteRepository.findByAmount(100)).thenReturn(bankNote);

        // Create an instance of the class under test


        // Call the method under test
        assertDoesNotThrow(() -> bankNoteService.deleteBankNote(100, mockHttpServletRequest));

        // Verify that the repository's delete method was called with the correct argument
        verify(bankNoteRepository).delete(any(BankNote.class));
    }

}
