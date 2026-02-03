import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { profile } from '../../props/profile';
import { creds } from '../../props/creds';
import { error } from '../../props/error';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [RouterModule, ReactiveFormsModule]
})
export class LoginComponent implements OnInit {
  httperror!: error;
  loginForm!: FormGroup;
  loggedInUser!: profile;

  constructor(private formBuilder: FormBuilder, private service: InstagramServiceService, private router: Router) {}

  ngOnInit(): void {
    this.service.loginUser$.subscribe(user => this.loggedInUser = user);
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.pattern("[A-Za-z0-9_. ]{3,}")]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  login() {
    const creds: creds | any = {
      username: this.loginForm.controls['username'].value,
      password: this.loginForm.controls['password'].value
    };
    this.service.login(creds).subscribe({
        next: (resp) => {
          this.loggedInUser = resp;        
          this.service.loginUser$.next(this.loggedInUser);
          this.router.navigate(['/home']);
        },
        error: (err) => this.httperror = err
      });
  }
}
