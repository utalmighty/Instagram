import { profile } from "./profile";

export class post {
    id!: string;
    userId!: string;
    links!: string[];
    postContent!: string;
    likeCount!: number;
    commentCount!: number;
    viewCount!: number;
    timestamp!: string;
    isActive!: boolean;
    profile!: profile;
    isLiked!: boolean;
}