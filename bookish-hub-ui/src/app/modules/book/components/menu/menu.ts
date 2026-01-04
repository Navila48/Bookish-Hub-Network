import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {TokenService} from '../../../../services/token/token-service';


@Component({
  selector: 'app-menu',
  imports: [
    RouterLink
  ],
  templateUrl: './menu.html',
  styleUrl: './menu.scss'
})
export class Menu implements OnInit{
  userName: string | null = null;
constructor(private tokenService: TokenService,private router:Router) {
}
  ngOnInit(): void {

      this.userName = this.tokenService.getUsername();

      const linkColor = document.querySelectorAll('.nav-link');
      linkColor.forEach(link => {
    /*    if (window.location.href.endsWith(link.getAttribute('href') || '')) {
          link.classList.add('active');
        } */
        link.addEventListener('click', () => {
          linkColor.forEach(l => l.classList.remove('active'));
          link.classList.add('active');
        });
      });
  }


  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['login']);
  }

  protected readonly localStorage = localStorage;
}
