$(function() {
	var $table = $('#thetable');
	if ($table.length) {
		console.log("inside table");

		$table.DataTable({

			lengthMenu : [ [ 3, 5, 10, -1 ],
					[ '3 Records', '5 Records', '10 Records', 'ALL' ] ]
			

		});
	}
});