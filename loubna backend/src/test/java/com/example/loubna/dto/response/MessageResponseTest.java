package com.example.loubna.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MessageResponse DTO Tests")
class MessageResponseTest {

    @Test
    @DisplayName("Should create MessageResponse with constructor")
    void testConstructor() {
        String expectedMessage = "Operation successful";
        MessageResponse response = new MessageResponse(expectedMessage);
        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    @DisplayName("Should set and get message correctly")
    void testMessageGetterSetter() {
        MessageResponse response = new MessageResponse("initial");
        String newMessage = "Updated message";
        
        response.setMessage(newMessage);
        
        assertEquals(newMessage, response.getMessage());
    }

    @Test
    @DisplayName("Should handle null message in constructor")
    void testNullMessageInConstructor() {
        MessageResponse response = new MessageResponse(null);
        assertNull(response.getMessage());
    }

    @Test
    @DisplayName("Should handle null message in setter")
    void testNullMessageInSetter() {
        MessageResponse response = new MessageResponse("initial");
        response.setMessage(null);
        assertNull(response.getMessage());
    }

    @Test
    @DisplayName("Should handle empty message")
    void testEmptyMessage() {
        MessageResponse response = new MessageResponse("");
        assertEquals("", response.getMessage());
    }

    @Test
    @DisplayName("Should handle message with special characters")
    void testMessageWithSpecialCharacters() {
        String message = "Erreur: L'utilisateur n'existe pas! @#$%";
        MessageResponse response = new MessageResponse(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should handle message with unicode characters")
    void testUnicodeMessage() {
        String message = "成功 🎉 Успешно";
        MessageResponse response = new MessageResponse(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should handle message with HTML content")
    void testHtmlMessage() {
        String message = "<strong>Error:</strong> Invalid input";
        MessageResponse response = new MessageResponse(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should handle very long message")
    void testVeryLongMessage() {
        String longMessage = "A".repeat(10000);
        MessageResponse response = new MessageResponse(longMessage);
        assertEquals(10000, response.getMessage().length());
    }

    @Test
    @DisplayName("Should handle message with line breaks")
    void testMessageWithLineBreaks() {
        String message = "Line 1\nLine 2\r\nLine 3";
        MessageResponse response = new MessageResponse(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should handle message with tabs")
    void testMessageWithTabs() {
        String message = "Column1\tColumn2\tColumn3";
        MessageResponse response = new MessageResponse(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should update message multiple times")
    void testMultipleUpdates() {
        MessageResponse response = new MessageResponse("First");
        
        response.setMessage("Second");
        assertEquals("Second", response.getMessage());
        
        response.setMessage("Third");
        assertEquals("Third", response.getMessage());
    }

    @Test
    @DisplayName("Should handle success message format")
    void testSuccessMessageFormat() {
        String message = "Projet créé avec succès !";
        MessageResponse response = new MessageResponse(message);
        assertTrue(response.getMessage().contains("succès"));
    }

    @Test
    @DisplayName("Should handle error message format")
    void testErrorMessageFormat() {
        String message = "Erreur : Utilisateur non trouvé.";
        MessageResponse response = new MessageResponse(message);
        assertTrue(response.getMessage().startsWith("Erreur"));
    }
}
