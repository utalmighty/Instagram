import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { profile } from '../../props/profile';
import { creds } from '../../props/creds';
import { error } from '../../props/error';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [RouterModule, ReactiveFormsModule, CommonModule]
})
export class LoginComponent implements OnInit {
  httperror!: error;
  loginForm!: FormGroup;
  user!: profile;

  constructor(private formBuilder: FormBuilder, private service: InstagramServiceService, private router: Router) {
    service.loginUser$.subscribe(r=> this.user = r);
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.pattern("[A-Za-z0-9_. ]{3,}")]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    })
  }

  login(){
    const creds: creds | any = {
      username: this.loginForm.controls['username'].value,
      password: this.loginForm.controls['password'].value
    };
    this.service.login(creds).subscribe(
      (resp) => { 
        this.user = resp;        
        this.service.loginUser$.next(this.user);
        this.router.navigate(['/home']);
      },
      (err)=> this.httperror = err.error
    );
  }

}
