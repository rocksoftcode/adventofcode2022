def input = new File('input.txt').text.split(/\n/)
def trail = []
def contents = [:]
def encode = {it.join('/').replaceAll('//', '/')}
input.each {
	def line = it.split(/\s/)
	if (line[0] == '$') {
		if (line[1] == 'cd' && line[2] == '..') {
			trail.removeLast()
		} else if (line[1] == 'cd') {
			trail << line[2]
		} else if (line[1] == 'ls') {
			contents[encode(trail)] = 0
		}
	} else if (line[0].isNumber()) {
		def crawl = trail.collect()
		while (crawl) {
			contents[encode(crawl)] += line[0].toInteger()
			crawl.removeLast()
		}
	}
}
println contents.entrySet()*.value.findAll { it >= contents['/']-70000000+30000000 }.sort()[0]