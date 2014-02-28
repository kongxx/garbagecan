
document.addEventListener('DOMContentLoaded', function(){
	chrome.tabs.getSelected(null, function(tab) {
		var url = tab.url;
		var isbn = null;

		if (url.indexOf('www.amazon.cn') > 0) {
			chrome.tabs.executeScript(tab.id, {file: "func.js", allFrames: true}, function(isbn) {
				if (isbn != null) {
					$.ajax({
						type: 'GET',
						url:   "http://api.douban.com/v2/book/isbn/" + isbn,
						success: function (data) {
							if (data != null) {
								var s = '';
								s += '<div>豆瓣评分：'+data['rating']['average']+'</div>';
								s += '<div><a href="http://book.douban.com/subject/'+data['id']+'" target="_blank">详细</a></div>';
								document.getElementById('myid').innerHTML = s;
							}
							//alert(data);
							//alert(data['rating']);
							//alert(data['rating']['average']);
						}
					});
				}
			});
		}
		
		if (url.indexOf('book.douban.com/subject/') > 0) {
			chrome.tabs.executeScript(tab.id, {file: "func.js", allFrames: true}, function(isbn) {
				if (isbn != null) {
					var amazon_url = "http://www.amazon.cn/gp/search/ref=sr_adv_b/?search-alias=stripbooks&unfiltered=1&field-title=&field-author=&field-keywords=&field-isbn="+isbn+"&field-publisher=&node=&field-price=&field-pct-off=&field-condition-type=&field-binding_browse-bin=&field-dateyear=&field-datemod=&field-dateop=&sort=relevancerank&imageField.x=20&imageField.y=10&__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99";
					var dangdang_url = "http://search.dangdang.com/index?key1=&key4="+isbn+"&key2=&key3=&category_path=01.00.00.00.00.00&medium=01";
					var jd_url = "http://search.jd.com/bookadvsearch?keyword=&author=&publisher=&isbn="+isbn+"&discount=&ep1=&ep2=&ap1=&ap2=&pty1=&ptm1=&pty2=&ptm2=";
					var chinapub_url = "http://search.china-pub.com/s/?key1="+isbn+"&type=&ts=&nb=&sp=&ep=&sps=&eps=&zks=&zke=&ordertype=&cbds=&cbde=&iy=0&t=2";
					
					var s = '';
					s += "<div>";
					s += "<ul>";
					s += '<li><a href="'+amazon_url+'" target="_blank">亚马逊</a></li>';
					s += '<li><a href="'+dangdang_url+'" target="_blank">当当</a></li>';
					s += '<li><a href="'+jd_url+'" target="_blank">京东</a></li>';
					s += '<li><a href="'+chinapub_url+'" target="_blank">China-Pub</a></li>';
					s += "</ul>";
					s += "</div>";
					document.getElementById('myid').innerHTML = s;
				}
			});
		}
	})
});


