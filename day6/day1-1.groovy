def input = new File('input.txt').text.split('').toList()
def bucket = new File('input.txt').text.split('').take(3).toList()
int marker = 3
while (marker < input.size()) {
	bucket << input[marker++]
	if (bucket.collect().unique().size() == 4) {
		println marker
		break
	}
	bucket.pop()
}