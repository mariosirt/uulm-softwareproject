class Matchconfig:

    def __init__(self, moledie_range: int, bowlerblade_range: int, bowlerblade_hit_chance: float,
                 bowlerblade_damage: int, lasercompact_hit_chance: float, rocketpen_damage: int, gasgloss_damage: int,
                 mothballpouch_range: int, mothballpouch_damage: int, fogtin_range: int, grapple_range: int,
                 grapple_hit_chance: float,
                 wiretapWithEarplugs_fail_chance: float, mirror_swap_chance: float, cocktail_dodge_chance: float,
                 cocktail_hp: int,
                 spy_success_chance: float, babysitter_success_chance: float, honeytrap_success_chance: float,
                 observation_success_chance: float,
                 chips_to_ip_factor: int, secret_to_ip_factor: int, min_chips_roulette: int, max_chips_roulette: int,
                 round_limit: int, turn_phase_limit: int,
                 cat_ip: int, strike_max: int, pause_limit: int, reconnect_limit: int):
        self.moledie_range = moledie_range
        self.bowlerblade_range = bowlerblade_range
        self.bowlerblade_hit_chance = bowlerblade_hit_chance
        self.bowlerblade_damage = bowlerblade_damage
        self.lasercompact_hit_chance = lasercompact_hit_chance
        self.rocketpen_damage = rocketpen_damage
        self.gasgloss_damage = gasgloss_damage
        self.motballpouch_range = mothballpouch_range
        self.mothballpouch_damage = mothballpouch_damage
        self.fogtin_range = fogtin_range
        self.grapple_range = grapple_range
        self.grapple_hit_chance = grapple_hit_chance
        self.wiretapWithEarplugs_fail_chance = wiretapWithEarplugs_fail_chance
        self.mirror_swap_chance = mirror_swap_chance
        self.cocktail_dodge_chance = cocktail_dodge_chance
        self.cocktail_hp = cocktail_hp
        self.spy_success_chance = spy_success_chance
        self.babysitter_success_chance = babysitter_success_chance
        self.honeytrap_success_chance = honeytrap_success_chance
        self.observation_success_chance = observation_success_chance
        self.chips_to_ip_factor = chips_to_ip_factor
        self.secret_to_ip_factor = secret_to_ip_factor
        self.min_chips_roulette = min_chips_roulette
        self.max_chips_roulette = max_chips_roulette
        self.round_limit = round_limit
        self.turn_phase_limit = turn_phase_limit
        self.cat_ip = cat_ip
        self.strike_max = strike_max
        self.pause_limit = pause_limit
        self.reconnect_limit = reconnect_limit
