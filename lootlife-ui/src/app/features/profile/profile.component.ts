import { Component, OnInit } from '@angular/core';
import { UserService } from '../../core/services/user.service';
import { UserResponseDto } from '../../models/user-response-dto.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{

  userLogged?: UserResponseDto;

  constructor(private _userService:UserService){}

  ngOnInit(): void {
    this.getUserInformation();
  }

  getUserInformation(){
    this._userService.getUserInformation().subscribe({
            next: (response: UserResponseDto) => {
              console.log(response);
              this.userLogged=response;
            },
            error: (error: any) => {
              console.error('Error', error.message);
              alert('Error ' + error.message);
            }
    })
  }

}
