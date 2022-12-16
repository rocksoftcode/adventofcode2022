def input = new File('input.txt').text.split(/\n/)
def nodes = []
input.eachWithIndex { it, i ->
	def valves = it.findAll(/[A-Z]{2}/)
	nodes << [id: i, name: valves[0], connections: valves[1..-1], rate: it.find(/\d+/).toInteger()]
}
def byName = nodes.collectEntries { [(it.name): it] }
def getActive = { nodes.findAll { it.rate > 0 } }
def calcDistance = { startName, distances = [:] ->
	if (byName[startName].distanceMap) return byName[startName].distanceMap
	def spread
	spread = { name, steps ->
		if (distances[name] && distances[name] <= steps) return
		distances[name] = steps
		byName[name].connections.each { n -> spread(n, steps+1) }
	}
	spread(startName, 0)
	byName[startName].distanceMap = distances
	distances
}

def compute = { timeRemaining ->
	def paths = [[curr: 'AA', active: getActive().collect { it.name}, timeLeft: timeRemaining, finished: false, steps: [], released: 0]]
	def max = 0
	for (def n = 0; n < paths.size(); n++) {
		def path = paths[n]
		if (path.timeLeft <= 0) path.finished = true
		if (path.finished) continue

		def distances = calcDistance(path.curr), moved = false
		path.active.each { act ->
			if (act == path.curr) return true
			if (path.timeLeft-distances[act] <= 1) return true
			moved = true
			paths << [
				curr: act,
				active: path.active.findAll { it != act },
				timeLeft: path.timeLeft-distances[act]-1,
				finished: false,
				steps: path.steps + [act],
				released: path.released + (path.timeLeft-distances[act]-1)*byName[act].rate
			]
		}
		if (!moved) path.finished = true
		if (path.finished && path.released > max) max = path.released
	}

	paths.findAll { p -> p.finished }.sort { a, b -> b.released-a.released  }
}
println compute(30)[0].released