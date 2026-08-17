package br.com.caphard.gameplay;

public enum HardRule {
	UNLOCK_DIFFICULTY("unlock_difficulty"),
	PLAYER_DAMAGE("player_damage"),
	MOUNT_DAMAGE("mount_damage"),
	SPEAR_DAMAGE("spear_damage"),
	ZOMBIE_IRON_EQUIPMENT("zombie_iron_equipment"),
	SPIDER_EFFECT("spider_effect"),
	TWELVE_WAVE_RAIDS("twelve_wave_raids"),
	WITCH_WEIGHT("witch_weight"),
	ENDERMAN_WEIGHT("enderman_weight"),
	MOUNT_TARGETING("mount_targeting"),
	HORSE_IMMUNITY("horse_immunity"),
	DEATH_ITEM_LOSS("death_item_loss");

	private final String id;

	HardRule(String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
	}

	public static HardRule byId(String id) {
		for (HardRule rule : values()) {
			if (rule.id.equals(id)) {
				return rule;
			}
		}
		return null;
	}
}
