def input = new File('input.txt').text.split(/\n/)*.split(',')*.collect {
	def bounds = it.split('-')*.toInteger()
	(bounds[0]..bounds[1]).toList()
}
println input.count {
	def combined = it[0] + it[1]
	combined.size() != combined.unique().size()
}