def input = new File('input.txt').readLines().collectEntries {
	def parts = it.split(/:\s/)
	def eq = parts[1].split(/\s/).toList()
	[parts[0], eq.size() == 1 ? parts[1] : eq]
}
def expr
expr = {map, k = 'root' ->
	def v = map[k] ?: k
	v instanceof List ? "(${v.collect {expr(map, it)}.join(' ')})" : v
}
println evaluate(expr(input))