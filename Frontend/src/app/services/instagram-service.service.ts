import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { profile } from '../props/profile';
import { simpleMessage } from '../props/simpleMessage';
import { comment } from '../props/comment';
import { post } from '../props/post';
import { creds } from '../props/creds';

@Injectable({
  providedIn: 'root'
})
export class InstagramServiceService {

  loginUser$: BehaviorSubject<profile> = new BehaviorSubject<profile>(new profile);

  private baseUrl: string = "http://localhost:9999/";
  constructor(private httpClient: HttpClient) { }

  postImage(fileObject: any): Observable<simpleMessage> {
    return this.httpClient.post<simpleMessage>(this.baseUrl + "huduk/sos", fileObject);
  }

  registerUser(profile: profile): Observable<profile> {
    return this.httpClient.post<profile>(this.baseUrl + "profiles/register", profile);
  }

  login(creds: creds): Observable<profile> {
    return this.httpClient.post<profile>(this.baseUrl + "profiles/authenticate", creds);
  }

  getUserDetails(userId: string): Observable<profile> {
    return this.httpClient.get<profile>(this.baseUrl + "profiles/userId/" + userId);
  }

  getUserDetailsByUsername(username: string): Observable<profile> {
    return this.httpClient.get<profile>(this.baseUrl + "profiles/username/" + username);
  }

  // TODO: Authenticate

  follow(follower: string, following: string): Observable<simpleMessage> {
    // TODO: Increase count of follower and following of profile object
    // Boolean
    return this.httpClient.post<simpleMessage>(this.baseUrl + "follow/" + follower + "/" + following, {});
  }

  isFollowing(follower: string, following: string): Observable<simpleMessage> {
    // Boolean
    return this.httpClient.get<simpleMessage>(this.baseUrl + "follow/" + follower + "/" + following);
  }

  followers(userId: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl + "follow/followers/" + userId);
  }

  followings(userId: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl + "follow/following/" + userId);
  }

  like(userId: string, postId: string): Observable<simpleMessage> {
    return this.httpClient.put<simpleMessage>(this.baseUrl + "post/like/" + postId + "/" + userId, {});
  }

  likestatus(userId: string, postId: string): Observable<simpleMessage> {
    return this.httpClient.get<simpleMessage>(this.baseUrl + "post/like/" + postId + "/" + userId);
  }

  view(postId: string): Observable<simpleMessage> {
    // Integer
    let queryParams = new HttpParams().append("postId", postId);
    return this.httpClient.put<simpleMessage>(this.baseUrl + "post/view", { param: queryParams });
  }

  comment(comment: comment): Observable<simpleMessage> {
    // Integer
    return this.httpClient.post<simpleMessage>(this.baseUrl + "post/comment", comment);
  }

  getComments(postId: string): Observable<comment[]> {
    return this.httpClient.get<comment[]>(this.baseUrl + "post/comment/" + postId);
  }

  postAPost(post: post): Observable<post> {
    return this.httpClient.post<post>(this.baseUrl + "post", post);
  }

  getAPost(postId: string): Observable<post> {
    return this.httpClient.get<post>(this.baseUrl + "post/" + postId);
  }

  getMultiplePost(postIds: string[]): Observable<post[]> {
    return this.httpClient.post<post[]>(this.baseUrl + "post/posts", postIds);
  }

  getPostsOfUser(userId: string): Observable<post[]> {
    return this.httpClient.get<post[]>(this.baseUrl + "post/user/" + userId);
  }

  getPostsByTag(tag: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl + "post/posts/tag/" + tag);
  }

  getFeed(userId: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl + "feed/user/" + userId);
  }

  getUsersLike(key: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl +"feed/search/username/" + key);
  }

  getHashesLike(key: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl +"feed/search/tag/" + key);
  }

  getTrendings(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl +"feed/trendsetter");
  }

}
