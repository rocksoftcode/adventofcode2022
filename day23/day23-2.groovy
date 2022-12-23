def key =  { x, y ->  y+':'+x }
def elves = [] as Set
new File('input.txt').text.split(/\n/).eachWithIndex{ line, y ->
	line.split('').eachWithIndex { v, x -> if (v == '#') elves << key(x,y) }
}
def directions = [
		N:  [dx:  0, dy: -1],
		NE: [dx:  1, dy: -1],
		E:  [dx:  1, dy:  0],
		SE: [dx:  1, dy:  1],
		S:  [dx:  0, dy:  1],
		SW: [dx: -1, dy:  1],
		W:  [dx: -1, dy:  0],
		NW: [dx: -1, dy: -1]
]
def locs = [
		[from: ['NW', 'NE', 'N'], to: 'N'],
		[from: ['SW', 'SE', 'S'], to: 'S'],
		[from: ['NW', 'SW', 'W'], to: 'W'],
		[from: ['SE', 'NE', 'E'], to: 'E'],
]

def start = 0
def run = {
	def considerations = [:]
	def destinations = [:]

	elves.each { e ->
		def coords = e.split(/:/)*.toInteger()
		def x = coords[1],y = coords[0]
		def adj = [] as Set

		directions.each { _k, dir -> if (elves.contains(key(x+dir.dx, y+dir.dy))) adj << _k }
		if (adj.size() == 0) return true
		for (def locId = start; locId < start+4; locId++) {
			def loc = locs[locId % 4];
			if (loc.from.every {!adj.contains(it) }) {
				def _k = key(x+directions[loc.to].dx, y+directions[loc.to].dy)
				if (!considerations[_k]) considerations[_k] = 0
				considerations[_k]++
				destinations[e] = _k
				break
			}
		}
	}

	def newPos = [] as Set
	def moved = false
	elves.each { e ->
		if (destinations[e] && considerations[destinations[e]] == 1) {
			newPos << destinations[e]
			moved = true
		} else newPos << e
	}

	start++
	[newPos, moved]
}

def rounds = 0
while (true) {
	rounds++
	def round = run()
	elves = round[0]
	if (!round[1]) break
}
println rounds
