import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  @ViewChild('audioPlayer', { static: false }) audioPlayer!: ElementRef<HTMLAudioElement>;

  /*Songs array in S3*/
  songs = [
    {
      songUrl: "https://lootlife-bucket.s3.us-east-1.amazonaws.com/Nuclear+Daisies+-+Fingers+in+Your+Flower+Crown+(Official+Audio)+-+portrayal+of+guilt.mp3",
      songName: "Nuclear Daisies - Finger in your flower crown"
    },
    {
      songUrl: "https://lootlife-bucket.s3.us-east-1.amazonaws.com/Martin+Dupont+-+Inside+Out+(1987)+-+e0d9n0b5.mp3",
      songName: "Martin Dupont - Inside Out"
    },
    {
      songUrl: "https://lootlife-bucket.s3.us-east-1.amazonaws.com/%5B%ED%95%9C%EA%B8%80+%EA%B0%80%EC%82%AC%EB%B2%88%EC%97%AD%5D+Joey+Valence+%26+Brae+-+LIVE+RIGHT+-+Nomeans.mp3",
      songName: "Joey Valence & Brae - LIVE RIGHT"
    },
    {
      songUrl: "https://lootlife-bucket.s3.us-east-1.amazonaws.com/Machine+Girl+-+ATHOTH+A+GO+GO!!+%5BExtended%5D+-+Accursed+Alucard.mp3",
      songName: "Machine Girl - ATHOTH A GO GO"
    },
    {
      songUrl: "https://lootlife-bucket.s3.us-east-1.amazonaws.com/Machine+Girl+-+Motherfather+(Official+Video)+-+Machine+Girl.mp3",
      songName: "Machine Girl - Motherfather"
    },
    {
      songUrl: "https://lootlife-bucket.s3.us-east-1.amazonaws.com/ESPRIT+%E7%A9%BA%E6%83%B3%2C+George+Clanton+-+Warmpop+-+100+Electronica.mp3",
      songName: " George Clanton - Warmpop"
    }
  ]

  constructor(private router:Router){}

  redirectAtLogin(){
    this.router.navigate(['/auth/login']);
  }

  redirectAtRegister(){
    this.router.navigate(['/auth/register']);
  }

  redirectAtProfile(){
    this.router.navigate(['/profile']);
  }

  redirectAtTask(){
    this.router.navigate(['/task']);
  }

  redirectAtMission(){
    this.router.navigate(['/mission']);
  }

  getRandomSongUrl(): string {
    const randomIndex = Math.floor(Math.random() * this.songs.length);
    return this.songs[randomIndex].songUrl;
  }

  playRandomSong(): void {
    if (this.audioPlayer) {
      const audio = this.audioPlayer.nativeElement;
      const randomSongUrl = this.getRandomSongUrl();

      // Pause actual song
      audio.pause();

      // Change song
      audio.src = randomSongUrl;

      // Load song
      audio.load();
      audio.play().catch(error => {
        console.error('Error al reproducir el audio:', error);
      });
    }
  }
}
