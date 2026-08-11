package fr.maxlego08.essentials.api.dto;

import java.util.UUID;

public record HomeShareDTO(UUID owner_id, String home_name, UUID target_id) {
}
