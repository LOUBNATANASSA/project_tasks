package com.example.loubna.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProjectRequest DTO Tests")
class ProjectRequestTest {

    private ProjectRequest projectRequest;

    @BeforeEach
    void setUp() {
        projectRequest = new ProjectRequest();
    }

    @Test
    @DisplayName("Should create ProjectRequest with default null values")
    void testDefaultConstructor() {
        assertNull(projectRequest.getTitle());
        assertNull(projectRequest.getDescription());
    }

    @Test
    @DisplayName("Should set and get title correctly")
    void testTitleGetterSetter() {
        String expectedTitle = "New Project";
        projectRequest.setTitle(expectedTitle);
        assertEquals(expectedTitle, projectRequest.getTitle());
    }

    @Test
    @DisplayName("Should set and get description correctly")
    void testDescriptionGetterSetter() {
        String expectedDescription = "This is a project description";
        projectRequest.setDescription(expectedDescription);
        assertEquals(expectedDescription, projectRequest.getDescription());
    }

    @Test
    @DisplayName("Should handle null title")
    void testNullTitle() {
        projectRequest.setTitle("Initial");
        projectRequest.setTitle(null);
        assertNull(projectRequest.getTitle());
    }

    @Test
    @DisplayName("Should handle null description")
    void testNullDescription() {
        projectRequest.setDescription("Initial");
        projectRequest.setDescription(null);
        assertNull(projectRequest.getDescription());
    }

    @Test
    @DisplayName("Should handle empty string title")
    void testEmptyTitle() {
        projectRequest.setTitle("");
        assertEquals("", projectRequest.getTitle());
    }

    @Test
    @DisplayName("Should handle empty string description")
    void testEmptyDescription() {
        projectRequest.setDescription("");
        assertEquals("", projectRequest.getDescription());
    }

    @Test
    @DisplayName("Should handle whitespace in title")
    void testWhitespaceTitle() {
        projectRequest.setTitle("   Project   ");
        assertEquals("   Project   ", projectRequest.getTitle());
    }

    @Test
    @DisplayName("Should handle special characters in title")
    void testSpecialCharactersInTitle() {
        String specialTitle = "Project @#$%^&*()_+-=[]{}|;':\",./<>?";
        projectRequest.setTitle(specialTitle);
        assertEquals(specialTitle, projectRequest.getTitle());
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        String unicodeTitle = "Projet avec émojis 🚀 et accents éàü";
        String unicodeDescription = "Description avec caractères spéciaux: αβγδ";
        
        projectRequest.setTitle(unicodeTitle);
        projectRequest.setDescription(unicodeDescription);
        
        assertEquals(unicodeTitle, projectRequest.getTitle());
        assertEquals(unicodeDescription, projectRequest.getDescription());
    }

    @Test
    @DisplayName("Should handle very long strings")
    void testVeryLongStrings() {
        String longTitle = "A".repeat(500);
        String longDescription = "B".repeat(10000);
        
        projectRequest.setTitle(longTitle);
        projectRequest.setDescription(longDescription);
        
        assertEquals(500, projectRequest.getTitle().length());
        assertEquals(10000, projectRequest.getDescription().length());
    }
}
