def input = new File('input.txt').text.split(/\n/)*.split(',').collect {it*.toInteger()}.toList()
def droplets = input.collect {[it[0] + 1, it[1] + 1, it[2] + 1]}
def maxX = droplets.max {it[0]}[0] + 1
def maxY = droplets.max {it[1]}[1] + 1
def maxZ = droplets.max {it[2]}[2] + 1
def all = (0..maxX + 1).collect {(0..maxY + 1).collect {(0..maxZ + 1).collect {false}}}
droplets.each {all[it[0]][it[1]][it[2]] = true}
def sides = 0
def transforms = [[-1, 0, 0], [1, 0, 0], [0, -1, 0], [0, 1, 0], [0, 0, -1], [0, 0, 1]]
def seen = all.collect {it.collect {it.collect {false}}}
def q = [[0, 0, 0]]
while (q) {
	def top = q.pop()
	def x = top[0]
	def y = top[1]
	def z = top[2]
	if (seen[x][y][z]) continue

	seen[x][y][z] = true
	for (t in transforms) {
		def xp = x + t[0]
		def yp = y + t[1]
		def zp = z + t[2]
		if (xp < 0 || yp < 0 || zp < 0 || xp > maxX || yp > maxY || zp > maxZ) continue

		if (all[xp][yp][zp]) {
			sides++
			continue
		}

		q << [xp, yp, zp]
	}
}

println sides
