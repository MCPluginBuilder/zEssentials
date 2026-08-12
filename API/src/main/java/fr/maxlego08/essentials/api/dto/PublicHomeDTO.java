package fr.maxlego08.essentials.api.dto;

import java.util.UUID;

public record PublicHomeDTO(UUID unique_id, String name, String location, String material, String category) {
}
