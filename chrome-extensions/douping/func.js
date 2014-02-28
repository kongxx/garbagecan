
var url = location.href;
if (url.indexOf("douban") >= 0) {
	var div = document.getElementById('info').innerHTML;
	var reg = /<span class="pl">ISBN:<\/span>(\s*)(\w*)</;
	var result = reg.exec(div);
	if (result != null && result.length == 3) { 
		isbn = result[2];
	}
} else {
	var div = document.getElementById('detail_bullets_id').innerHTML;
	var reg = /<li><b>ISBN:<\/b>(\s*)(\w*)(,?)(\s*)(\w*)<\/li>/;
	var result =  reg.exec(div);
	if (result != null && result.length == 6) {
		if (result[5] != null && result[5].length > 0) {
			isbn = result[5];
		} else if (result[2] != null && result[2].length > 0) {
			isbn = result[2];
		}
	}
}