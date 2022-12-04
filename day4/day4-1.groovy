def input = new File('input.txt').text.split(/\n/)*.split(',')*.collect {
	def bounds = it.split('-')*.toInteger()
	(bounds[0]..bounds[1]).toList()
}
println input.count {it[0](it[1]) || it[1].containsAll(it[0])}