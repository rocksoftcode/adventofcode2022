def caves = new File('input.txt').text.split(/\n/)*.split(' -> ')*.collect {it.split(',')*.toInteger()}
def cmax = { idx -> caves.collectMany { it }.max { it[idx] }[idx] }
def maxX = cmax(0)*2
def maxY = cmax(1)

def dropPoint = [500, 0]
def drop = { screen ->
		def sand = dropPoint.collect()
		def thru = false

	while (true) {
		if (sand[1] > maxY+1) {thru = true; break }
		if (screen[sand[1]+1][sand[0]] == 0) { sand[1]++; continue }
		if (screen[sand[1]+1][sand[0]-1] == 0) { sand[1]++; sand[0]--; continue }
		if (screen[sand[1]+1][sand[0]+1] == 0) { sand[1]++; sand[0]++; continue }
		break
	}

	screen[sand[1]][sand[0]] = 2
	return thru
}

def grid = (0..maxY+2).collect { (0..maxX-1).collect { 0 } }
def line = { from, to ->
	def min = [ Math.min(from[0], to[0]), Math.min(from[1], to[1]) ]
	def max = [ Math.max(from[0], to[0]), Math.max(from[1], to[1]) ]
	for (def i = min[0]; i <= max[0]; i++)
		for (def j = min[1]; j <= max[1]; j++)
			grid[j][i] = 1
}
caves.each { point -> point.eachWithIndex { p, i -> i && line(point[i-1] ?: p, p) } }
def drops = 0
while (!drop(grid)) drops++
println drops