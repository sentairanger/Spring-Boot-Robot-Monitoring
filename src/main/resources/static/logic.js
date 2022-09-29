$('#forward').on('mousedown', function(){
	$.get('/forward');
	});
$('#backward').on('mousedown', function(){
	$.get('/backward');
	});
$('#left').on('mousedown', function(){
	$.get('/left');
	});
$('#right').on('mousedown', function(){
	$.get('/right');
	});
$('#on').on('mousedown', function(){
	$.get('/blink');
	});