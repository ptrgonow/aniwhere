var currentMenu;
var menuLinks = document.querySelectorAll('.menu');

function clickMenuHandler(){
    if (currentMenu){
        currentMenu.classList.remove('menu-active');
    }
    this.classList.add('menu-active');
    currentMenu = this;
}

for (var i = 0; i < menuLinks.length; i++){
    menuLinks[i].addEventListener('click', clickMenuHandler);
}

const button = document.querySelectorAll('button');

button.forEach(button =>{
    button.addEventListener('click', (event) => {
        const para = button.nextElementSibling;
        const icon = button.children[1]

        para.classList.toggle('show');
        icon.classList.toggle('rotate');
    })
})