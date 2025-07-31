import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GroupService } from '../service/group.service';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-group',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-group.html',
  styleUrls: ['./add-group.css']
})
export class AddGroup {
  name: string = '';
  description: string = '';
  errorMessage = '';

  constructor(
    private groupService: GroupService,
    private authService: AuthService,
    private router: Router
  ) {}

  submitGroup() {
    const user = this.authService.user;
    if (!user || !user.id) {
      this.errorMessage = 'Niste prijavljeni.';
      return;
    }

    const groupDTO = {
      name: this.name,
      description: this.description,
      adminId: user.id,
      posts: [] 
    };

    this.groupService.addGroup(groupDTO).subscribe({
      next: (group) => {
        this.router.navigate(['/home']); 
      },
      error: (err) => {
        this.errorMessage = 'Greška pri kreiranju grupe.';
        console.error(err);
      }
    });
  }
}
