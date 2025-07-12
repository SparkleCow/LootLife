import { StatsResponseDto } from "./stats-response-dto.model";

export interface UserResponseDto {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  profileImageUrl: string;
  bannerImageUrl: string;
  age: number;
  birthDate: string;
  createdAt: string;
  lastModifiedAt: string;
  isVerified: boolean;
  isEnabled: boolean;
  roles: string[];
  stats: StatsResponseDto;
}
