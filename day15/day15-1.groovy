def distance = {a, b -> Math.abs(a.x - b.x) + Math.abs(a.y - b.y)}
def input = new File('input.txt').text.split(/\n/).collect {
	def points = it.findAll(/\-?\d+/)*.toInteger()
	def a = [x: points[0], y: points[1]]
	def b = [x: points[2], y: points[3]]
	[sensor: a, beacon: b, dist: distance(a, b)]
}
def noBeacon = [] as Set
def onLine = [] as Set
def y = 2_000_000
input.each {
	if (it.beacon.y == y) {
		onLine.add(it.beacon.x)
	}
	def min = distance(it.sensor, [x: it.sensor.x, y: y])
	if (min <= it.dist) {
		def aroundX = it.dist - min;
		for (def x = it.sensor.x - aroundX; x <= it.sensor.x + aroundX; x++) {
			noBeacon << x
		}
	}
}
println noBeacon.size() - onLine.size()
