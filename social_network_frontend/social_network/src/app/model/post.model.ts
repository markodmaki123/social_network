import { Reaction } from "./reaction.model";

export interface Post {
  id: number;
  content: string;
  creationDate: string;
  userId: number;
  groupId: number;
  reactions: Reaction[];
}