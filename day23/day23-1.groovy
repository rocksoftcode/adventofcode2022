def key = {x, y -> y + ':' + x}
def elves = [] as Set
new File('input.txt').text.split(/\n/).eachWithIndex {line, y ->
	line.split('').eachWithIndex {v, x -> if (v == '#') elves << key(x, y)}
}
def dirs = [
		N : [dx: 0, dy: -1],
		NE: [dx: 1, dy: -1],
		E : [dx: 1, dy: 0],
		SE: [dx: 1, dy: 1],
		S : [dx: 0, dy: 1],
		SW: [dx: -1, dy: 1],
		W : [dx: -1, dy: 0],
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
	def poss = [:]
	def dest = [:]

	elves.each {e ->
		def coords = e.split(/:/)*.toInteger()
		def x = coords[1], y = coords[0]
		def adj = [] as Set

		dirs.each {k, dir -> if (elves.contains(key(x + dir.dx, y + dir.dy))) adj << k}
		if (adj.size() == 0) return true
		for (def locId = start; locId < start + 4; locId++) {
			def loc = locs[locId % 4];
			if (loc.from.every {!adj.contains(it)}) {
				def _k = key(x + dirs[loc.to].dx, y + dirs[loc.to].dy)
				if (!poss[_k]) poss[_k] = 0
				poss[_k]++
				dest[e] = _k
				break
			}
		}
	}

	def newPos = [] as Set
	def moved = false
	elves.each {e ->
		if (dest[e] && poss[dest[e]] == 1) {
			newPos << dest[e]
			moved = true
		} else newPos << e
	}

	start++
	newPos
}

10.times {elves = run(elves)}
def minX = 4, maxX = 4, minY = 4, maxY = 4
elves.each {e ->
	def coords = e.split(':')*.toInteger()
	y = coords[0]
	x = coords[1]
	minX = Math.min(minX, x)
	minY = Math.min(minY, y)
	maxX = Math.max(maxX, x)
	maxY = Math.max(maxY, y)
}
println((1 + Math.abs(maxY - minY)) * (1 + Math.abs(maxX - minX)) - elves.size())
