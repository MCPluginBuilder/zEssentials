package fr.maxlego08.essentials.api.dto;

public record HomeDTO(String location, String name, String material, Boolean is_public, String category,
                      Boolean is_favorite) {
}
