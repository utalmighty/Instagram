import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InstagramServiceService } from '../../services/instagram-service.service';
import { profile } from '../../props/profile';
import { error } from '../../props/error';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule]
})
export class RegisterComponent {

  httperror!: error;
  registerForm!: FormGroup;
  user!: profile;

  constructor(private formBuilder: FormBuilder, private service: InstagramServiceService, private router: Router) {
    service.loginUser$.subscribe((a) => this.user = a);
  }

  ngOnInit(): void {
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
        this.user = resp;
        this.service.loginUser$.next(this.user);
        this.router.navigate(['/home']);
      },
      (err)=> this.httperror = err.error
    );
  }
}
