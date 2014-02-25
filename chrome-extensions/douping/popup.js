
document.addEventListener('DOMContentLoaded', function(){
	chrome.tabs.getSelected(null, function(tab) {
		var url = tab.url;
		var isbn = null;

		chrome.tabs.executeScript(tab.id, {file: "func.js", allFrames: true}, function(isbn) {
			if (isbn != null) {
				$.ajax({
					type: 'GET',
					url:   "http://api.douban.com/v2/book/isbn/" + isbn,
					success: function (data) {
						if (data != null) {
							var s = '';
							s += '<div>∂π∞Í∆¿∑÷£∫'+data['rating']['average']+'</div>';
							s += '<div><a href="http://book.douban.com/subject/'+data['id']+'" target="_blank">œÍœ∏</a></div>';
							document.getElementById('myid').innerHTML = s;
						}
						//alert(data);
						//alert(data['rating']);
						//alert(data['rating']['average']);
					}
				});
			}
		});
	})
});


