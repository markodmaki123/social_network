export interface Reaction {
  id: number;
  createdAt: string;
  userId: number;
  reactionType: 'LIKE' | 'DISLIKE' | 'HEART';
  postId: number;
}