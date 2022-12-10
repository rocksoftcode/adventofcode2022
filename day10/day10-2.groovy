def input = new File('input.txt').text.split(/\n/).toList()
def ops = [:]
def cycle = 1
input.each {it ->
	if (it != 'noop') {
		cycle += 2
		ops[cycle] = it.split(/\s/)[1].toInteger()
	} else cycle++
}
def x = 1
def corr = {i -> (i % 40) - 1}
(1..240).each {
	x += ops[it] ? ops[it] : 0
	print corr(it) in (x - 1..x + 1) ? '#' : '.'
	if (it % 40 == 0) println ''
}
