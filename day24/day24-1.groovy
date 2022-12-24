def input = new File('input.txt').text.split(/\n/)[1..-2].collect {it[1..-2].split('').collect {['>': 0, v: 1, '<': 2, '^': 3, '.': -1][it]}}
def gcd
gcd = {a, b -> b ? gcd(b, a % b) : a}
def lcm = {a, b -> a * b / gcd(a, b)}
def dirs = [[0, 1], [1, 0], [0, -1], [-1, 0]]
def openings = {
	def rows = input.size()
	def columns = input[0].size()
	def cycle = lcm(rows, columns)
	def blizzards = []
	input.eachWithIndex {row, r ->
		row.eachWithIndex {e, c ->
			if (e != -1) {
				blizzards << [r: r, c: c, transform: dirs[e]]
			}
		}
	}
	return (0..cycle).collect {
		def map = (0..rows).collect {(0..columns).collect {1}}
		blizzards.each {b ->
			map[b.r][b.c] = 0
			b.r += b.transform[0]
			b.c += b.transform[1]
			if (b.r == -1) {
				b.r = rows - 1
			}
			if (b.r == rows) {
				b.r = 0
			}
			if (b.c == -1) {
				b.c = columns - 1
			}
			if (b.c == columns) {
				b.c = 0
			}
		}
		map
	}
}

def run = {
	def rows = input.size()
	def columns = input[0].size()
	def cycle = lcm(rows, columns).toInteger()
	def opens = openings()
	def movements = dirs.collect() + [[0, 0]]
	def startR = -1
	def startC = 0
	def targetR = rows
	def targetC = columns - 1
	def states = [[startR, startC]]
	def steps = 0
	while (states.size() > 0) {
		def seen = [:]
		for (def r = -2; r < rows + 2; r++) {
			seen[r] = []
		}
		def nextStates = []
		steps++
		def safe = opens[(steps + 1) % cycle]
		for (def state in states) {
			def r = state[0]
			def c = state[1]
			for (def transform in movements) {
				def nextR = r + transform[0]
				def nextC = c + transform[1]
				if (nextR == targetR && nextC == targetC) {
					return steps + 1
				}
				if ((nextC < 0 || !seen[nextR][nextC]) && ((nextR == startR && nextC == startC) || (nextR >= 0 && nextC >= 0 && nextR < rows && nextC < columns && safe[nextR][nextC]))) {
					seen[nextR][nextC] = true
					nextStates.push([nextR, nextC])
				}
			}
		}
		states = nextStates
	}
	steps
}
println run()