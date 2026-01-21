import {Component, OnInit} from '@angular/core';
import { RouterLink} from '@angular/router';
import {KeycloakService} from '../../../../services/keycloak/keycloakService';


@Component({
  selector: 'app-menu',
  imports: [
    RouterLink
  ],
  templateUrl: './menu.html',
  styleUrl: './menu.scss'
})
export class Menu implements OnInit{
  username = '' ;
constructor(private keycloakService: KeycloakService) {
}
  ngOnInit(): void {

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

get userName(): string{
  const tokenParsed = this.keycloakService.keycloak?.tokenParsed;
  this.username = tokenParsed?.['given_name'] ?? null;
  return this.username;
}
   logout() {
    return this.keycloakService.logout();
  }

}
