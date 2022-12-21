def input = new File('input.txt').readLines().collectEntries {
	def parts = it.split(/:\s/)
	def eq = parts[1].split(/\s/).toList()
	[parts[0], eq.size() == 1 ? new BigInteger(parts[1]) : eq]
}
input.root[1] = '-'
input.humn = 'x'

def expr
expr = {map, k = 'root' ->
	def v = map[k] ?: k
	v instanceof List ? "(${v.collect {p -> expr(map, p)}.join(' ')})" : v
}
def search
search = {cmp,
          inv = false,
          min = new BigInteger("0"),
          max = new BigInteger(Long.MAX_VALUE.toString())
	->
	while (min != max) {
		def pivot = ((min + max) / 2).trunc()
		def v = cmp(pivot)
		if (v == 0) return pivot
		if (v < 0) {
			if (min == pivot)
				return inv
						? null
						: search({-cmp(it)}, true)
			min = pivot
		} else {
			max = ((min + max) / 2).trunc()
		}
	}
}

def start = {exp -> search({x -> Eval.x(x, exp)})}
println start(expr(input))