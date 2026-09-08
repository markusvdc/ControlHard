package br.com.caphard.gameplay;

public enum HardRule {
	UNLOCK_DIFFICULTY("unlock_difficulty"),
	PLAYER_DAMAGE("player_damage"),
	CREEPER_DAMAGE("creeper_damage"),
	MOUNT_DAMAGE("mount_damage"),
	SPEAR_DAMAGE("spear_damage"),
	ZOMBIE_IRON_EQUIPMENT("zombie_iron_equipment"),
	SPIDER_EFFECT("spider_effect"),
	PHANTOM_SPEED("phantom_speed"),
	TWELVE_WAVE_RAIDS("twelve_wave_raids"),
	WITCH_WEIGHT("witch_weight"),
	WITCH_SLOWNESS_DURATION("witch_slowness_duration"),
	ENDERMAN_WEIGHT("enderman_weight"),
	MOUNT_TARGETING("mount_targeting"),
	HORSE_IMMUNITY("horse_immunity"),
	CAPPED_EXPERIENCE_COST("capped_experience_cost"),
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
