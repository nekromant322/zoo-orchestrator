function getAll(){
    $.ajax(
        {
            URL: 'http://localhost:8080/api/film/get?id=1',
            type: 'GET',
            complete: function data(data) {
                alert(JSON.stringify(data));
            }

        }
);

}