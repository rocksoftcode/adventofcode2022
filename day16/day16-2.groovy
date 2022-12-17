def input = new File('input.txt').text.split(/\n/)
def nodes = []
input.eachWithIndex {it, i ->
	def valves = it.findAll(/[A-Z]{2}/)
	nodes << [id: i, name: valves[0], connections: valves[1..-1], rate: it.find(/\d+/).toInteger()]
}
def byName = nodes.collectEntries {[(it.name): it]}
def getActive = {nodes.findAll {it.rate > 0}}
def calcDistance = {startName, distances = [:] ->
	if (byName[startName].distanceMap) return byName[startName].distanceMap
	def spread
	spread = {name, steps ->
		if (distances[name] && distances[name] <= steps) return
		distances[name] = steps
		byName[name].connections.each {n -> spread(n, steps + 1)}
	}
	spread(startName, 0)
	byName[startName].distanceMap = distances
	distances
}

def compute = {timeRemaining ->
	def paths = [[curr: 'AA', active: getActive().collect {it.name}, timeRemaining: timeRemaining, finished: false, steps: [], released: 0]]
	def max = 0
	for (def i = 0; i < paths.size(); i++) {
		def path = paths[i]
		if (path.timeRemaining <= 0) path.finished = true
		if (path.finished) continue

		def distances = calcDistance(path.curr)
		def moved = false
		path.active.each {active ->
			if (active == path.curr || path.timeRemaining - distances[active] <= 1) return
			moved = true
			paths << [
					curr         : active,
					active       : path.active.findAll {it != active},
					timeRemaining: path.timeRemaining - distances[active] - 1,
					finished     : false,
					steps        : path.steps + [active],
					released     : path.released + (path.timeRemaining - distances[active] - 1) * byName[active].rate
			]
		}
		if (!moved) path.finished = true
		if (path.finished && path.released > max) max = path.released
	}

	paths.findAll {p -> p.finished}.sort {a, b -> b.released - a.released}
}


def paths = compute(26)
def max = 0

for (def i = 0; i < paths.size(); i++)
	for (def j = i + 1; j < paths.size(); j++)
		if (paths[i].steps.every {!paths[j].steps.contains(it)})
			if (paths[i].released + paths[j].released > max) {
				max = paths[i].released + paths[j].released
			}

println max