def input = new File('input.txt').text.split(/\n/).toList()
def ops = [:]
def cycle = 1
input.each {it ->
	if (it != 'noop') {
		cycle += 2
		ops[cycle] = it.split(/\s/)[1].toInteger()
	} else cycle++
}
def b = [20, 60, 100, 140, 180, 220]
def x = 1
def s = 0
(1..b.last()).each {
	x += ops[it] ? ops[it] : 0
	if (it in b) s += it * x
}
println s
