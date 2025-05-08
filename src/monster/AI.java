//package monster;
//
//import Main.gamePanel;
//import entity.Entity;
//import object.Bomb;
//import java.util.*;
//
//public class AI {
//
//    private final gamePanel gp;
//    private final Random rand = new Random();
//
//    // Constants
//    private static final int BOMB_ESCAPE_RANGE = 2 * 48;
//    private static final int ESCAPE_DURATION = 120;
//    private static final int SAFE_DISTANCE_FROM_BOMB = 3 * 48;
//    private static final int TILE_SIZE = 48;
//
//    // AI state
//    private boolean isEscapingBomb = false;
//    private int escapeCounter = 0;
//    private List<Node> currentPath = new ArrayList<>();
//    private int pathIndex = 0;
//    private int lastPlayerX, lastPlayerY;
//
//    public AI(Entity entity, gamePanel gp) {
//        this.entity = entity;
//        this.gp = gp;
//        this.lastPlayerX = -1;
//        this.lastPlayerY = -1;
//    }
//
//    public void updateAI() {
//        if (entity.moving) return;
//
//        if (isEscapingBomb) {
//            handleBombEscaping();
//            return;
//        }
//
//        Bomb bomb = findNearbyBomb();
//        if (bomb != null) {
//            startEscaping(bomb);
//            return;
//        }
//
//        if (isPlayerInRange()) {
//            chasePlayer();
//        } else {
//            randomMovement();
//        }
//    }
//
//    private void handleBombEscaping() {
//        if (escapeCounter++ > ESCAPE_DURATION) {
//            resetEscapeState();
//            return;
//        }
//
//        followPath();
//    }
//
//    private void resetEscapeState() {
//        isEscapingBomb = false;
//        escapeCounter = 0;
//        currentPath.clear();
//        pathIndex = 0;
//    }
//
//    private void startEscaping(Bomb bomb) {
//        isEscapingBomb = true;
//        escapeCounter = 0;
//
//        int bombX = bomb.worldX / TILE_SIZE;
//        int bombY = bomb.worldY / TILE_SIZE;
//        int entityX = entity.worldX / TILE_SIZE;
//        int entityY = entity.worldY / TILE_SIZE;
//
//        Node bestTarget = findSafeLocation(bombX, bombY, entityX, entityY);
//        if (bestTarget != null) {
//            currentPath = findPath(entityX, entityY, bestTarget.x, bestTarget.y);
//            pathIndex = 0;
//        }
//    }
//
//    private Node findSafeLocation(int bombX, int bombY, int entityX, int entityY) {
//        List<Node> potentialTargets = new ArrayList<>();
//        int searchRadius = SAFE_DISTANCE_FROM_BOMB / TILE_SIZE;
//
//        // Search in concentric circles starting from safe distance
//        for (int distance = searchRadius; distance <= searchRadius + 2; distance++) {
//            for (int dx = -distance; dx <= distance; dx++) {
//                for (int dy = -distance; dy <= distance; dy++) {
//                    if (Math.abs(dx) + Math.abs(dy) >= distance) {
//                        int tx = bombX + dx;
//                        int ty = bombY + dy;
//                        if (isValidTile(tx, ty)){
//                            potentialTargets.add(new Node(tx, ty));
//                        }
//                    }
//                }
//            }
//
//            if (!potentialTargets.isEmpty()) {
//                break; // Found some targets at this distance
//            }
//        }
//
//        // Find closest safe location
//        Node bestTarget = null;
//        int minDistance = Integer.MAX_VALUE;
//
//        for (Node target : potentialTargets) {
//            int distance = Math.abs(target.x - entityX) + Math.abs(target.y - entityY);
//            if (distance < minDistance) {
//                minDistance = distance;
//                bestTarget = target;
//            }
//        }
//
//        return bestTarget;
//    }
//
//    private Bomb findNearbyBomb() {
//        for (Entity entity : gp.bombManager.bombList[gp.currentMap]) {
//            if (entity instanceof Bomb) {
//                int dx = Math.abs(this.entity.worldX - entity.worldX);
//                int dy = Math.abs(this.entity.worldY - entity.worldY);
//                if (dx <= BOMB_ESCAPE_RANGE && dy <= BOMB_ESCAPE_RANGE) {
//                    return (Bomb) entity;
//                }
//            }
//        }
//        return null;
//    }
//
//
//
//    private void chasePlayer() {
//        int entityX = entity.worldX / TILE_SIZE;
//        int entityY = entity.worldY / TILE_SIZE;
//        int playerX = gp.player.worldX / TILE_SIZE;
//        int playerY = gp.player.worldY / TILE_SIZE;
//
//        // Only recalculate path if player moved significantly or we finished current path
//        boolean playerMoved = (Math.abs(playerX - lastPlayerX) > 1 || Math.abs(playerY - lastPlayerY) > 1);
//        boolean needNewPath = currentPath.isEmpty() || pathIndex >= currentPath.size() || playerMoved;
//
//        if (needNewPath) {
//            lastPlayerX = playerX;
//            lastPlayerY = playerY;
//            currentPath = findPath(entityX, entityY, playerX, playerY);
//            pathIndex = 0;
//        }
//
//        followPath();
//    }
//
//    private void followPath() {
//        if (pathIndex >= currentPath.size()) return;
//
//        Node nextNode = currentPath.get(pathIndex);
//        int nextX = nextNode.x * TILE_SIZE;
//        int nextY = nextNode.y * TILE_SIZE;
//
//        // Check if reached current node
//        if (Math.abs(entity.worldX - nextX) < TILE_SIZE && Math.abs(entity.worldY - nextY) < TILE_SIZE) {
//            pathIndex++;
//            if (pathIndex >= currentPath.size()) return;
//            nextNode = currentPath.get(pathIndex);
//            nextX = nextNode.x * TILE_SIZE;
//            nextY = nextNode.y * TILE_SIZE;
//        }
//
//        // Determine direction
//        String newDirection = calculateDirection(nextX, nextY);
//
//        if (!entity.direction.equals(newDirection)) {
//            entity.direction = newDirection;
//        }
//
//        // Check collision
//        entity.collisionOn = false;
//        gp.checker.checkTile(entity);
//
//        if (!entity.collisionOn) {
//            startMoving();
//        } else {
//            // Path blocked, find new path
//            currentPath.clear();
//            pathIndex = 0;
//        }
//    }
//
//    private String calculateDirection(int targetX, int targetY) {
//        int dx = targetX - entity.worldX;
//        int dy = targetY - entity.worldY;
//
//        if (Math.abs(dx) > Math.abs(dy)) {
//            return dx > 0 ? "right" : "left";
//        } else {
//            return dy > 0 ? "down" : "up";
//        }
//    }
//
//    private void startMoving() {
//        entity.moving = true;
//        entity.pixelCounter = 0;
//    }
//
//    private void randomMovement() {
//        if (rand.nextInt(100) < 95) return;
//
//        String[] directions = {"up", "down", "left", "right"};
//        entity.direction = directions[rand.nextInt(4)];
//        entity.collisionOn = false;
//        gp.checker.checkTile(entity);
//
//        if (!entity.collisionOn) {
//            startMoving();
//        }
//    }
//
//    // Optimized A* Pathfinding
//    private List<Node> findPath(int startX, int startY, int targetX, int targetY) {
//        if (!isValidTile(targetX, targetY)) {
//            return new ArrayList<>();
//        }
//
//        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
//        Map<String, Node> allNodes = new HashMap<>();
//
//        Node start = new Node(startX, startY);
//        start.gScore = 0;
//        start.fScore = heuristic(start, targetX, targetY);
//        allNodes.put(start.getKey(), start);
//        openSet.add(start);
//
//        while (!openSet.isEmpty()) {
//            Node current = openSet.poll();
//
//            if (current.x == targetX && current.y == targetY) {
//                return reconstructPath(current);
//            }
//
//            for (Node neighbor : getNeighbors(current)) {
//                String neighborKey = neighbor.getKey();
//                Node neighborNode = allNodes.computeIfAbsent(neighborKey, k -> neighbor);
//
//                if (!isValidTile(neighborNode.x, neighborNode.y)) continue;
//
//                int tentativeGScore = current.gScore + 1;
//
//                if (tentativeGScore < neighborNode.gScore) {
//                    neighborNode.cameFrom = current;
//                    neighborNode.gScore = tentativeGScore;
//                    neighborNode.fScore = tentativeGScore + heuristic(neighborNode, targetX, targetY);
//
//                    if (!openSet.contains(neighborNode)) {
//                        openSet.add(neighborNode);
//                    }
//                }
//            }
//        }
//
//        return new ArrayList<>();
//    }
//
//    private List<Node> reconstructPath(Node endNode) {
//        LinkedList<Node> path = new LinkedList<>();
//        Node current = endNode;
//
//        while (current != null) {
//            path.addFirst(current);
//            current = current.cameFrom;
//        }
//
//        return path.size() > 1 ? path.subList(1, path.size()) : new ArrayList<>();
//    }
//
//    private List<Node> getNeighbors(Node node) {
//        return Arrays.asList(
//                new Node(node.x + 1, node.y),
//                new Node(node.x - 1, node.y),
//                new Node(node.x, node.y + 1),
//                new Node(node.x, node.y - 1)
//        );
//    }
//
//    private int heuristic(Node node, int targetX, int targetY) {
//        return Math.abs(node.x - targetX) + Math.abs(node.y - targetY);
//    }
//
//    private boolean isValidTile(int x, int y) {
//        if (x < 0 || y < 0 || x >= gp.maxWorldCol || y >= gp.maxWorldRow) {
//            return false;
//        }
//
//        // Cache collision checks
//        int oldX = entity.worldX;
//        int oldY = entity.worldY;
//        entity.worldX = x * TILE_SIZE;
//        entity.worldY = y * TILE_SIZE;
//        entity.collisionOn = false;
//        gp.checker.checkTile(entity);
//        entity.worldX = oldX;
//        entity.worldY = oldY;
//
//        return !entity.collisionOn && !isBombAt(x, y);
//    }
//
//    private boolean isBombAt(int x, int y) {
//        for (Entity entity : gp.bombManager.bombList[gp.currentMap]) {
//            if (entity instanceof Bomb) {
//                int bombX = entity.worldX / TILE_SIZE;
//                int bombY = entity.worldY / TILE_SIZE;
//                if (bombX == x && bombY == y) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private static class Node {
//        final int x, y;
//        int gScore = Integer.MAX_VALUE;
//        int fScore = Integer.MAX_VALUE;
//        Node cameFrom = null;
//
//        Node(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//
//        String getKey() {
//            return x + "," + y;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Node)) return false;
//            Node node = (Node) o;
//            return x == node.x && y == node.y;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(x, y);
//        }
//    }
//}
