function getLoginStatus(){
    $.ajax({
        type: 'get',
        url: 'login',
        success: function(){
            console.log('你已经登录了！')
        },
        error: function(){
            location.assign('login.html')
        }
    })
}