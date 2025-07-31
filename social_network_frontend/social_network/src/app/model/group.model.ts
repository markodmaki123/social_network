import { Post } from "./post.model";

export interface Group {
  id: number;
  name: string;
  description: string;
  creationDate: string;
  adminId: number;
  posts: Post[];
}