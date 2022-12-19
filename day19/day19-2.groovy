class Blueprint {
	int id
	int costOre
	int costClay
	int[] costObsidian
	int[] costGeode
}

def blueprints = new File('input.txt').readLines().collect {
	def m = it.findAll(/\d+/)*.toInteger()
	new Blueprint(id: m[0], costOre: m[1], costClay: m[2], costObsidian: [m[3], m[4]], costGeode: [m[5], m[6]])
}

def test = {blueprint, i ->
	def calcMax = [
			ore : [
					blueprint.costOre,
					blueprint.costClay,
					blueprint.costObsidian[0],
					blueprint.costGeode[0]
			].max(),
			clay: blueprint.costObsidian[1],
	]

	def geodeMax = 0
	def search
	search = {time, oreRobots, clayRobots, obsidianRobots, ore, clay, obsidian, geodes ->
		if (time < 1) return

		if (geodes + (time * (time + 1)) / 2 < geodeMax) {
			return
		}
		if (geodes > geodeMax) {
			geodeMax = geodes
		}

		if (obsidianRobots > 0) {
			def buildGeode =
					blueprint.costGeode[0] <= ore && blueprint.costGeode[1] <= obsidian
			def skip =
					1 +
							(buildGeode
									? 0
									: Math.max(
									Math.ceil((blueprint.costGeode[0] - ore) / oreRobots),
									Math.ceil((blueprint.costGeode[1] - obsidian) / obsidianRobots)
							))

			search(
					time - skip,
					oreRobots,
					clayRobots,
					obsidianRobots,
					ore + skip * oreRobots - blueprint.costGeode[0],
					clay + skip * clayRobots,
					obsidian + skip * obsidianRobots - blueprint.costGeode[1],
					geodes + time - skip
			)

			if (buildGeode) return
		}

		if (clayRobots > 0) {
			def buildObsidian =
					blueprint.costObsidian[0] <= ore && blueprint.costObsidian[1] <= clay
			def skip =
					1 +
							(buildObsidian
									? 0
									: Math.max(
									Math.ceil((blueprint.costObsidian[0] - ore) / oreRobots),
									Math.ceil((blueprint.costObsidian[1] - clay) / clayRobots)
							))

			if (time - skip > 2) {
				search(
						time - skip,
						oreRobots,
						clayRobots,
						obsidianRobots + 1,
						ore + skip * oreRobots - blueprint.costObsidian[0],
						clay + skip * clayRobots - blueprint.costObsidian[1],
						obsidian + skip * obsidianRobots,
						geodes
				)
			}
		}

		if (clayRobots < calcMax.clay) {
			def buildClay = blueprint.costClay <= ore
			def skip =
					1 + (buildClay ? 0 : Math.ceil((blueprint.costClay - ore) / oreRobots))

			if (time - skip > 3) {
				search(
						time - skip,
						oreRobots,
						clayRobots + 1,
						obsidianRobots,
						ore + skip * oreRobots - blueprint.costClay,
						clay + skip * clayRobots,
						obsidian + skip * obsidianRobots,
						geodes
				)
			}
		}

		if (oreRobots < calcMax.ore) {
			def buildOre = blueprint.costOre <= ore
			def skip =
					1 + (buildOre ? 0 : Math.ceil((blueprint.costOre - ore) / oreRobots))

			if (time - skip > 4) {
				search(
						time - skip,
						oreRobots + 1,
						clayRobots,
						obsidianRobots,
						ore + skip * oreRobots - blueprint.costOre,
						clay + skip * clayRobots,
						obsidian + skip * obsidianRobots,
						geodes
				)
			}
		}
	}

	search(i, 1, 0, 0, 0, 0, 0, 0)
	return geodeMax
}

def result = 1
3.times {
	result *= test(blueprints[it], 32)
}
println result.toInteger()