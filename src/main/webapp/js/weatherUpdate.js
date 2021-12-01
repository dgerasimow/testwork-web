$(document).ready(function ()
{
    $('#weather').submit( function(event)
    {
        event.preventDefault();
        var weatherData = {
            city: $("#city").val(),
        }

        $.post(
            "/weather",
            weatherData,
            func,
            "json"
        )

        function func(responseData)
        {
            // var data = JSON.parse(responseData)
            console.log(responseData)
            $("#cityName").text(responseData.city)
            $("#temperature").text(responseData.temperature)
            $("#currWeather").text(responseData.weather_main)
            $("#weatherDesc").text(responseData.weather_desc)

        }

    });
});