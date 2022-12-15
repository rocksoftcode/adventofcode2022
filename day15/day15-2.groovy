def distance = {a, b -> Math.abs(a.x - b.x) + Math.abs(a.y - b.y)}
def input = new File('input.txt').text.split(/\n/).collect {
	def points = it.findAll(/\-?\d+/)*.toInteger()
	def a = [x: points[0], y: points[1]]
	def b = [x: points[2], y: points[3]]
	[sensor: a, beacon: b, dist: distance(a, b)]
}
def max = input.size() == 14 ? 20 : 4000000
for (def y = 0; y < max; y++) {
	def intervals = []
	input.each {
		def minDist = distance(it.sensor, [x: it.sensor.x, y: y])
		if (minDist <= it.dist) {
			def distAroundX = it.dist - minDist
			def from = it.sensor.x - distAroundX
			def to = it.sensor.x + distAroundX

			intervals << [from, to]
		}
	}

	intervals.sort {a, b -> a[0] - b[0]}
	for (def i = 1; i < intervals.size(); i++) {
		if (intervals[i - 1][1] >= intervals[i][0]) {
			intervals[i - 1][1] = Math.max(intervals[i - 1][1], intervals[i][1])
			intervals.removeAt(i)
			i--
		}
	}

	def res = []
	for (def interval in intervals) {
		if (interval[0] > max || 0 > interval[1]) {
			continue
		}
		res << [Math.max(interval[0], 0), Math.min(interval[1], max)]
	}

	if (res.size() > 1) {
		def x = res[0][1] + 1
		println(x * 4_000_000 + y)
		System.exit(0)
	}
}