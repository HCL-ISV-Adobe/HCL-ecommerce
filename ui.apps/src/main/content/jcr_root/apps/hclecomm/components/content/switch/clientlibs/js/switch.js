$(function() {
    if(checkmode !== "edit") { 
    var darkMode = localStorage.getItem('darkMode');
    if(darkMode == 'enabled') {
        document.documentElement.setAttribute('data-theme', 'dark');
    }
    //in principle will have only one component for one page.
    var darkThemebtn = document.querySelectorAll('.dark-mode-toggle');
    darkThemebtn.forEach(item => {
        item.addEventListener('click', function(event) {
            //console.log(event.target);

            if(document.documentElement.getAttribute('data-theme') 
                && document.documentElement.getAttribute('data-theme') == 'dark') {
                document.documentElement.setAttribute('data-theme', 'light');
                localStorage.setItem('darkMode', null);
            } else {
                document.documentElement.setAttribute('data-theme', 'dark');
                localStorage.setItem('darkMode', 'enabled');
            }

            /*if(this.checked) {
                //trans()
                document.documentElement.setAttribute('data-theme', 'dark')
            } else {
                //trans()
                document.documentElement.setAttribute('data-theme', 'light')
            }*/

        });
    });
    } 
});