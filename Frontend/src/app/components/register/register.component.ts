import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { profile } from '../../props/profile';
import { error } from '../../props/error';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [ReactiveFormsModule, RouterModule]
})
export class RegisterComponent implements OnInit, OnDestroy {

  httperror!: error;
  registerForm!: FormGroup;
  loggedInUser!: profile;

  constructor(private formBuilder: FormBuilder, private service: InstagramServiceService, private router: Router) {}

  ngOnInit(): void {
    this.service.loginUser$.subscribe(user => this.loggedInUser = user);
    this.registerForm = this.formBuilder.group({
      fullname: ['', [Validators.required, Validators.pattern("[A-Za-z ]{3,}")]],
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.pattern("[A-Za-z0-9_. ]{3,}")]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    })
  }

  register() {
    const profile: profile | any = {
      fullName: this.registerForm.controls['fullname'].value,
      email: this.registerForm.controls['email'].value,
      username: this.registerForm.controls['username'].value,
      password: this.registerForm.controls['password'].value
    };
    this.service.registerUser(profile).subscribe(
      (resp) => { 
        this.loggedInUser = resp;
        this.service.loginUser$.next(this.loggedInUser);
        this.router.navigate(['/home']);
      },
      (err)=> this.httperror = err.error
    );
  }

  ngOnDestroy(): void {
    if (this.service.loginUser$) {
      this.service.loginUser$.unsubscribe();
    }
  }
}
